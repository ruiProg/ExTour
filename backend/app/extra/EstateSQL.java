package extra;

import com.fasterxml.jackson.databind.JsonNode;

import models.Category;

public class EstateSQL {

    public static boolean processCategories(JsonNode catJSON){

        catJSON = catJSON.get("Categories");
        if(catJSON.isArray()){
            for(JsonNode cat: catJSON){
                Category newCat = new Category(cat.get("ID").toString(),cat.get("Titulo").toString());
                newCat.save();
            }
            return true;
        }
        return false;
    }
}
