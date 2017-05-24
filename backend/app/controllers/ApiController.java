package controllers;

import com.avaje.ebean.ExpressionList;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;
import java.util.StringJoiner;

import models.Category;
import models.Council;
import models.District;
import models.Estate;
import models.Parish;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

public class ApiController extends Controller {


    public Result getCategories() {

        List<Category> categoryList = Category.find.all();
        ArrayNode catJSON = Json.newArray();
        for(Category cat : categoryList){
            ObjectNode category = Json.newObject();
            category.put("id",cat.id);
            category.put("name",cat.title);
            category.put("protected",cat.isProtected);
            catJSON.add(category);
        }
        return ok(catJSON);
    }

    public Result getRegions(String id) {

        ArrayNode regionsJSON = Json.newArray();
        if (id.equals("-1")){
            List<District> districtList = District.find.all();
            for(District dist : districtList){
                ObjectNode district = Json.newObject();
                district.put("id",dist.id);
                district.put("name",dist.name);
                regionsJSON.add(district);
            }
        }
        else if(id.length() == 2){
            District district = District.find.byId(id);
            if(district != null){
                for(Council coun : district.councils){
                    ObjectNode council = Json.newObject();
                    council.put("id",coun.id);
                    council.put("name",coun.name);
                    regionsJSON.add(council);
                }
            }
            else return notFound("There is no such district");
        }
        else if(id.length() == 4){
            Council council = Council.find.byId(id);
            if(council != null){
                for(Parish par : council.parishes){
                    ObjectNode parish = Json.newObject();
                    parish.put("id",par.id);
                    parish.put("name",par.name);
                    regionsJSON.add(parish);
                }
            }
            else return notFound("There is no such council");
        }
        else return badRequest("Cannot understand request ID");
        return ok(regionsJSON);
    }

    public Result getPOIS(List<String> categories, List<String> regions, String tag) {

        ArrayNode poisJSON = Json.newArray();
        String estateExpression = new String();
        boolean statementDefined = false;

        if(categories.size() > 0){
            StringJoiner joiner = new StringJoiner(",");
            for (String str : categories)
                joiner.add(str);
            estateExpression += "category IN(" + joiner.toString() + ")";
            statementDefined = true;
        }

        if(regions.size() > 0){
            StringJoiner joiner = new StringJoiner(",");
            for (String str : regions){
                if(str.length() == 2){
                    List<Council> councils = District.find.byId(str).councils;
                    for(Council c : councils){
                         List<Parish> parishes = Council.find.byId(c.id).parishes;
                        for(Parish p : parishes)
                            joiner.add(p.id);
                    }
                }
                else if(str.length() == 4){
                    List<Parish> parishes = Council.find.byId(str).parishes;
                    for(Parish p : parishes)
                        joiner.add(p.id);
                }
                joiner.add(str);
            }
            estateExpression += "parish IN(" + joiner.toString() + ")";
            statementDefined = true;
        }

        if(tag != null && tag.length() > 0){
            if(statementDefined)
                estateExpression += " AND";
            estateExpression += " MATCH (title, details) AGAINST(\"" + tag + "\")";
        }

        List<Estate> pois;
        if(estateExpression.length() > 0)
            pois = Estate.find.where().raw(estateExpression).findList();
        else pois = Estate.find.all();

        for(Estate estate:pois) {
            ObjectNode poi = Json.newObject();
            poi.put("id", estate.id);
            poi.put("title", estate.title);
            poi.put("category", estate.category.title);
            poi.put("parish", estate.parish.name);
            Council council = estate.parish.council;
            poi.put("council", council.name);
            District dis = council.district;
            poi.put("district", dis.name);
            poi.put("imageURL", estate.imageURL);
            poisJSON.add(poi);
        }
        return ok(poisJSON);
    }

    public Result getPOI(String id){

        Estate estate = Estate.find.byId(id);
        if(estate != null){
            ObjectNode poi = Json.newObject();
            poi.put("id", estate.id);
            poi.put("title", estate.title);
            poi.put("category", estate.category.title);
            poi.put("parish", estate.parish.name);
            Council council = estate.parish.council;
            poi.put("council", council.name);
            District dis = council.district;
            poi.put("district", dis.name);
            poi.put("details", estate.details);
            poi.put("imageURL", estate.imageURL);
            poi.put("linkURL", estate.linkURL);
            poi.put("coords", estate.coords);
            return ok(poi);
        }
        else return badRequest("invalid POI id");
    }
}
