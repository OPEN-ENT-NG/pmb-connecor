package fr.openent.pmb;

import fr.openent.pmb.controllers.PmbController;
import io.vertx.core.json.JsonObject;
import org.entcore.common.http.BaseServer;

public class Pmb extends BaseServer {

    @Override
    public void start() throws Exception {
        super.start();

        JsonObject exportConfig = config.getJsonObject("export");

        addController(new PmbController(exportConfig));
    }

}
