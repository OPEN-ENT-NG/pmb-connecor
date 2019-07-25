package fr.openent.pmb.services;

import fr.wseduc.webutils.Either;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;

public interface ExportService {

    /**
     * Get structure user based on structure UAI
     *
     * @param UAI     Structure UAI
     * @param handler Function handler returning data
     */
    void getUser(String uai, Handler<Either<String, JsonArray>> handler);
}
