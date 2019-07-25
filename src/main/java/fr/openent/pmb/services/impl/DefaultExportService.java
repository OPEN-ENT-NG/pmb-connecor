package fr.openent.pmb.services.impl;

import fr.openent.pmb.services.ExportService;
import fr.wseduc.webutils.Either;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.entcore.common.neo4j.Neo4j;
import org.entcore.common.neo4j.Neo4jResult;

public class DefaultExportService implements ExportService {

    private JsonObject config;

    public DefaultExportService(JsonObject exportConfig) {
        this.config = exportConfig;
    }

    @Override
    public void getUser(String uai, Handler<Either<String, JsonArray>> handler) {
        String query = "MATCH(s:Structure {UAI: {uai}})<-[:DEPENDS]-(g:ManualGroup {name:{group_name}})<-[:IN]-(u:User) " +
                "RETURN u.externalId as externalId, u.firstName as firstName, u.lastName as lastName, u.login as login, CASE WHEN u.emailInternal IS NULL THEN u.email ELSE u.emailInternal END as emailInterne;";

        JsonObject params = new JsonObject()
                .put("uai", uai)
                .put("group_name", config.getString("group_name"));

        Neo4j.getInstance().execute(query, params, Neo4jResult.validResultHandler(handler));
    }
}
