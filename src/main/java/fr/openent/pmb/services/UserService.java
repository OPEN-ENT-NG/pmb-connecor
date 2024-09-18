package fr.openent.pmb.services;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;

public interface UserService {
    /**
     * Get structure user detailed based on structure UAI
     * @param users   User list to complete with infos
     * @param UAIs    UAI list of structures
     * @param type    Profile of users to complete (Student / Teacher / Personnel)
     */
    Future<JsonArray> mergeUserInfos(JsonArray users, JsonArray UAIs, String type);
}
