package fr.openent.pmb.services;

import fr.wseduc.webutils.Either;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;


public interface ExportService {

    /**
     * Get structure user based on structure UAI
     *
     * @param UAIs    UAI list of structures
     * @param type    Type of manager
     * @param handler Function handler returning data
     */
    void getUser(JsonArray UAIs, String type, Handler<Either<String, JsonArray>> handler);

    void retrieveDeployedStructures(Handler<Either<String, JsonArray>> handler);
}
