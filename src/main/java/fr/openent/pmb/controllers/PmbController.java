package fr.openent.pmb.controllers;

import fr.openent.pmb.services.ExportService;
import fr.openent.pmb.services.impl.DefaultExportService;
import fr.openent.pmb.worker.AmassWorker;
import fr.wseduc.bus.BusAddress;
import fr.wseduc.rs.Get;
import fr.wseduc.security.SecuredAction;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonArray;
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

    @BusAddress("fr.openent.pmb.controllers.PmbController|amass")
    public void amass(Message<JsonObject> message) {
        log.info("Starting PMB amass");
        exportService.retrieveDeployedStructures(handler -> {
            if (handler.isLeft()) {
                log.error("Unable to retrieve deployed structures", handler.left().getValue());
                return;
            }

            JsonArray structures = handler.right().getValue();
            JsonObject map = new JsonObject();
            structures.forEach(structure -> {
                JsonObject s = (JsonObject) structure;
                map.put(s.getString("uai"), s.getString("id"));
            });

            JsonObject workerConfig = new JsonObject()
                    .put("structures", map);
            DeploymentOptions options = new DeploymentOptions()
                    .setConfig(workerConfig)
                    .setWorker(true);
            vertx.deployVerticle(AmassWorker::new, options);
        });

        message.reply(new JsonObject().put("status", "accepted"));
    }
}
