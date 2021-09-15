package fr.openent.pmb.services.impl;

import fr.openent.pmb.Pmb;
import fr.openent.pmb.services.ExportService;
import fr.wseduc.webutils.Either;
import io.vertx.core.Handler;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.entcore.common.neo4j.Neo4j;
import org.entcore.common.neo4j.Neo4jResult;


public class DefaultExportService implements ExportService {

    private final JsonObject exportConfig;

    public DefaultExportService(JsonObject exportConfig) {
        this.exportConfig = exportConfig;
    }

    @Override
    public void getUser(JsonArray UAIs, String type, Handler<Either<String, JsonArray>> handler) {
        String query = "MATCH (s:Structure)<-[:DEPENDS]-(g:ManualGroup {name:{group_name}})<-[:IN]-(u:User) " +
                "WHERE s.UAI IN {uais} " +
                "RETURN DISTINCT u.externalId as externalId, u.firstName as firstName, u.lastName as lastName, u.login as login, " +
                "CASE WHEN u.emailInternal IS NULL THEN u.email ELSE u.emailInternal END as emailInterne;";

        JsonObject params = new JsonObject().put("uais", UAIs);

        // Type of manager
        if (type == null || type.isEmpty()) {
            params.put("group_name", exportConfig.getString("group_manager"));
        }
        else {
            switch (type) {
                case Pmb.MANAGER_TYPE_PRET :
                    params.put("group_name", exportConfig.getString("group_manager_lend_manual"));
                    break;
                case Pmb.MANAGER_TYPE_CDI :
                    params.put("group_name", exportConfig.getString("group_manager"));
                    break;
                default:
                    handler.handle(new Either.Left<>("[PMB@getUser] The type parameter '" + type + "' is unknown."));
            }
        }

        Neo4j.getInstance().execute(query, params, Neo4jResult.validResultHandler(handler));
    }

    @Override
    public void retrieveDeployedStructures(Handler<Either<String, JsonArray>> handler) {
        String query = "MATCH(s:Structure) WHERE HAS(s.exports) AND 'PMB' IN s.export RETURN DISTINCT s.UAI as uai, s.id as id";
        Neo4j.getInstance().execute(query, new JsonObject(), Neo4jResult.validResultHandler(handler));
    }
}
