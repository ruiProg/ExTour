package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.concurrent.CompletionStage;

import play.Application;
import javax.inject.Inject;
import javax.inject.Provider;

import play.Logger;
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

    public Result parseRegions() {

        AreaSQL regions = new AreaSQL(appProvider.get().getFile("conf/AreaID.txt"));
        if(regions.process())
            return ok("Regions parsed");
        return internalServerError("File could not be parsed");
    }

   public Result parseCategories() {

       String categoriesURL = "http://dadosabertos.bitcliq.com/GeoDasCategories.ashx";
       WSRequest request = ws.url(categoriesURL);
       request.setQueryParameter("t","PAT")
               .setQueryParameter("token","A45E9DBA-0AD4-4A1B-AEBD-ED9E58FDF2F0")
               .setQueryParameter("sid","2");

       CompletionStage<JsonNode> catPromise = request.get().thenApply(WSResponse::asJson);
       catPromise.thenAccept((catJSON) -> Logger.error(catJSON.toString()));
       return ok("Ok");
   }
}
