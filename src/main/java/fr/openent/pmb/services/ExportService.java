package fr.openent.pmb.services;

import fr.wseduc.webutils.Either;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;

import java.util.List;


public interface ExportService {

    /**
     * Get structure user based on structure UAI
     *
     * @param UAIs    UAI list of structures
     * @param type    Type of manager
     * @param handler Function handler returning data
     */
    void getUser(JsonArray UAIs, String type, Handler<Either<String, JsonArray>> handler);

    /**
     * Get structure user based on structure UAI
     *
     * @param uais    UAI list of structures
     * @param type    Type of manager
     * @return Future Function handler returning data
     */
    Future<JsonArray> getUsers(JsonArray uais, String type);

    /**
     * Get structure user based on structure UAI
     *
     * @param userIds   list of user identifiers
     * @return Future Function handler returning data
     */
    Future<JsonArray> getUserLendGroupUAIs(List<String> userIds);

    void retrieveDeployedStructures(Handler<Either<String, JsonArray>> handler);
}
