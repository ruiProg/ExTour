package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.CompletionStage;

import extra.EstateSQL;
import play.Application;
import javax.inject.Inject;
import javax.inject.Provider;

import play.libs.ws.WSClient;
import play.libs.ws.WSRequest;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Result;

import extra.AreaSQL;

public class DevController extends Controller {

    @Inject
    private Provider<Application> appProvider;

    @Inject
    private WSClient ws;

    private final String iGeoTag = "PAT";
    private final String token = "A45E9DBA-0AD4-4A1B-AEBD-ED9E58FDF2F0";

    public Result parseRegions() {

        AreaSQL regions = new AreaSQL(appProvider.get().getFile("conf/AreaID.txt"));
        if(regions.process())
            return ok("Regions parsed");
        return internalServerError("File could not be parsed");
    }

   public Result parseCategories() {

       final String iGeoURL = "http://dadosabertos.bitcliq.com/GeoDasCategories.ashx";

       WSRequest request = ws.url(iGeoURL);
       request.setQueryParameter("t",iGeoTag)
               .setQueryParameter("token",token)
               .setQueryParameter("sid","2");

       CompletionStage<JsonNode> catPromise = request.get().thenApply(WSResponse::asJson);
       CompletionStage<Boolean> ret = catPromise.thenApply(EstateSQL::processCategories);

       WSRequest requestPro = ws.url(iGeoURL);
       requestPro.setQueryParameter("t",iGeoTag)
               .setQueryParameter("token",token)
               .setQueryParameter("sid","1");

       CompletionStage<JsonNode> catProPromise = requestPro.get().thenApply(WSResponse::asJson);
       CompletionStage<Boolean> retPro = catProPromise.thenApply(EstateSQL::processCategories);

       try {
           if(ret.toCompletableFuture().get() && retPro.toCompletableFuture().get())
               return ok("Categories parsed");
       } catch (Exception e) {
           e.printStackTrace();
       }
       return internalServerError("Categories request could not be parsed");
   }


   public void parseEstates(JsonNode estatesJSON){

       final String iGeoURL = "http://dadosabertos.bitcliq.com/GeoDas.ashx";

       estatesJSON = estatesJSON.get("Objects");
       if(estatesJSON.isArray()){
           for(JsonNode estate: estatesJSON) {
               String id = estate.get("ID").asText();
               WSRequest request = ws.url(iGeoURL);
               request.setQueryParameter("t",iGeoTag)
                       .setQueryParameter("token",token)
                       .setQueryParameter("sdid",id);

               CompletionStage<JsonNode> estatePromise = request.get().thenApply(WSResponse::asJson);
               CompletionStage<Boolean> ret = estatePromise.thenApply(EstateSQL::processEstate);

               try {
                   ret.toCompletableFuture().get();
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
       }
   }

   public Result parseEstates(String cat) {

       final String iGeoURL = "http://dadosabertos.bitcliq.com/GeoDas.ashx";

           WSRequest request = ws.url(iGeoURL);
           request.setQueryParameter("t", iGeoTag)
                   .setQueryParameter("token", token)
                   .setQueryParameter("cats", cat + ";")
                   .setRequestTimeout(600000); //10min

           CompletionStage<JsonNode> estatesPromise = request.get().thenApply(WSResponse::asJson);
           estatesPromise.thenAcceptAsync(this::parseEstates);

       return ok("Estates saved");
   }

   public Result parseEstatesFile(String idFile){

       try {
           String content = new Scanner(appProvider.get().getFile("conf/estatesFile_"+idFile+".txt"), "UTF-8").useDelimiter("\\Z").next();
           ObjectMapper mapper = new ObjectMapper();
           JsonNode estatesJSON = mapper.readTree(content);
           parseEstates(estatesJSON);
       } catch (Exception e) {
           e.printStackTrace();
       }
       return ok("Estates saved");
   }
}
