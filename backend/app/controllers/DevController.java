package controllers;

import play.Application;
import javax.inject.Inject;
import javax.inject.Provider;
import play.mvc.Controller;
import play.mvc.Result;

import extra.AreaSQL;

public class DevController extends Controller {

    @Inject
    private Provider<Application> appProvider;

    public Result parseRegions() {

        AreaSQL regions = new AreaSQL(appProvider.get().getFile("conf/AreaID.txt"));
        if(regions.process())
            return ok("Regions parsed");
        return internalServerError("File could not be parsed");
    }
}
