package extra;

import com.fasterxml.jackson.databind.JsonNode;

import models.Category;
import models.Council;
import models.District;
import models.Estate;
import models.Parish;

public class EstateSQL {

    public static boolean processCategories(JsonNode catJSON){

        catJSON = catJSON.get("Categories");
        if(catJSON.isArray()){
            for(JsonNode cat: catJSON){
                Category newCat = new Category(cat.get("ID").asText(),cat.get("Titulo").asText());
                newCat.save();
            }
            return true;
        }
        return false;
    }

    public static boolean processEstate(JsonNode estateJSON){

        String parishID = "-1";
        String location = estateJSON.get("Resumo").asText();
        String[] locationArray = location.split("\\s*,\\s*");
        if(locationArray.length >= 4) {

            //some parishes contain "," so locationArray[3] may not contain the full parish name
            locationArray[3] = location.substring(location.lastIndexOf(locationArray[3]));

            int islandTerm = locationArray[1].indexOf('(');
            if(islandTerm != -1)
                locationArray[1] = locationArray[1].substring(0,islandTerm);
            District district = District.find.where().eq("name", locationArray[1]).findUnique();

            for(Council council: district.councils)
                if(council.name.equals(locationArray[2])) {

                    for(Parish parish:council.parishes)
                        if(parish.name.toLowerCase().equals(locationArray[3].toLowerCase()))
                            parishID = parish.id;
                }
        }

        Estate newEstate = new Estate(estateJSON.get("ID").asText(),estateJSON.get("Titulo").asText(),
                estateJSON.get("CategoryID").asText(), parishID,
                estateJSON.get("Detalhes").asText(),estateJSON.get("URL_Imagem").asText(),
                estateJSON.get("Link").asText(),estateJSON.get("Gps").asText());
        newEstate.save();
        return true;
    }
}
