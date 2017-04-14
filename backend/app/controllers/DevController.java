package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

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
       CompletionStage<Boolean> ret = catPromise.thenApply(EstateSQL::processCategories);

       WSRequest requestPro = ws.url(categoriesURL);
       requestPro.setQueryParameter("t","PAT")
               .setQueryParameter("token","A45E9DBA-0AD4-4A1B-AEBD-ED9E58FDF2F0")
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
}
