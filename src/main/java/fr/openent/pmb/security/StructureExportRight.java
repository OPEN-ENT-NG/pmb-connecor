package fr.openent.pmb.security;

import fr.wseduc.webutils.http.Binding;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import org.entcore.common.http.filter.ResourcesProvider;
import org.entcore.common.user.UserInfos;

import java.util.Map;


public class StructureExportRight implements ResourcesProvider {
    @Override
    public void authorize(HttpServerRequest resourceRequest, Binding binding, UserInfos user, Handler<Boolean> handler) {
        Map<String, UserInfos.Function> functions = user.getFunctions();
        if (functions != null && !functions.isEmpty() && functions.containsKey("SUPER_ADMIN")) {
                handler.handle(true);
        } else {
                handler.handle(WorkflowActionUtils.hasRight(user, WorkflowActions.STRUCTURE_EXPORT_RIGHT.toString()));
        }
    }
}