package fr.openent.pmb.controllers;

import fr.openent.pmb.services.ExportService;
import fr.openent.pmb.services.impl.DefaultExportService;
import fr.wseduc.rs.Get;
import fr.wseduc.security.SecuredAction;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import org.entcore.common.controller.ControllerHelper;

import static org.entcore.common.http.response.DefaultResponseHandler.arrayResponseHandler;

public class PmbController extends ControllerHelper {

    private ExportService exportService;

    public PmbController(JsonObject exportConfig) {
        super();
        exportService = new DefaultExportService(exportConfig);
    }

    @Get("")
    public void render(HttpServerRequest request) {
        renderView(request);
    }

    @Get("/gestionnaire/list")
    @SecuredAction("pmb.structure.export")
    public void listGestionnaire(HttpServerRequest request) {
        String uai = request.getParam("uai");
        exportService.getUser(uai, arrayResponseHandler(request));
    }
}
