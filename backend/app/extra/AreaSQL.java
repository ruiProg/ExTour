package extra;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import models.Council;
import models.District;
import models.Parish;
import play.Logger;

public class AreaSQL{

    private File source;
    private static final int maxDistrict = 3;
    private static final int maxCouncil = 5;

    public AreaSQL(File pSource){

        source = pSource;
    }

    private String parseCapital(String txt){

        txt = txt.toLowerCase();
        if(txt.length() > 0) {
            char[] charArray = txt.toCharArray();
            charArray[0] = Character.toUpperCase(charArray[0]);
            int i = 1;
            while(i < charArray.length){
                if(!Character.isLetter(charArray[i]) && i + 1 < charArray.length) {
                    boolean found = false;
                    if (charArray[i + 1] == 'd' && i + 3 < charArray.length){
                        if(charArray[i + 2] == 'a' || charArray[i + 2] == 'e' || charArray[ i + 2] == 'o') {
                            if (!Character.isLetter(charArray[i + 3]))
                                found = true;
                            else if(i + 4 < charArray.length && charArray[i + 3] == 's'){
                                if (!Character.isLetter(charArray[i + 4]))
                                    found = true;
                            }
                        }
                    }
                    else if (charArray[i + 1] == 'e' && i + 2 < charArray.length){
                        if (!Character.isLetter(charArray[i + 2]))
                            found = true;
                    }
                    if (!found && Character.isLetter(charArray[i + 1])) {
                        charArray[i + 1] = Character.toUpperCase(charArray[i + 1]);
                        i++;
                    }
                }
                i++;
            }
            return new String(charArray);
        }
        return "";
    }

    public boolean process(){

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(source), StandardCharsets.UTF_8));
            String line = reader.readLine();
            String currDistrict = "";
            String currCouncil = "";

            while (line != null) {
                int idIndex = line.indexOf('\t');
                String id = line.substring(0,idIndex);
                String name = line.substring(idIndex+1);

                if(idIndex <= maxDistrict){
                    currDistrict = id;
                    District newDistrict = new District(id,name);
                    newDistrict.save();
                }
                else if (idIndex <= maxCouncil){
                    currCouncil = id;
                    Council newCouncil = new Council(id,name, currDistrict);
                    newCouncil.save();
                }
                else{
                    //some parishes are in capital
                    name = parseCapital(name);
                    Parish newParish = new Parish(id,name, currCouncil);
                    newParish.save();
                }

                line = reader.readLine();
            }
        }
        catch(Exception e){
            Logger.error(e.toString());
            return false;
        }
        return true;
    }
}