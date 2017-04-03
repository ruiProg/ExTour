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