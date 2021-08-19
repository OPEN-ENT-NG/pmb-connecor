package fr.openent.pmb;

import fr.openent.pmb.controllers.PmbController;
import fr.openent.pmb.server.PMBServer;
import io.vertx.core.json.JsonObject;
import org.entcore.common.http.BaseServer;

public class Pmb extends BaseServer {
    public static final String MANAGER_TYPE_PRET = "pret";
    public static final String MANAGER_TYPE_CDI = "cdi";

    @Override
    public void start() throws Exception {
        super.start();

        JsonObject exportConfig = config.getJsonObject("export");
        JsonObject PMBConfig = config.getJsonObject("PMB");
        PMBServer.getInstance().init(vertx, PMBConfig);

        addController(new PmbController(exportConfig));
    }

}
