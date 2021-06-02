package fr.openent.pmb;

import fr.openent.pmb.controllers.EmailSendController;
import fr.openent.pmb.controllers.PmbController;
import fr.openent.pmb.controllers.SchoolController;
import fr.openent.pmb.server.PMBServer;
import fr.wseduc.webutils.email.EmailSender;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import org.entcore.common.email.EmailFactory;
import org.entcore.common.http.BaseServer;

public class Pmb extends BaseServer {

    public static String DB_SCHEMA;
    public static String SCHOOL_TABLE;

    public static final String STRUCTURE_EXPORT_RIGHT = "pmb.structure.export";

    public static final String MANAGER_TYPE_PRET = "pret";
    public static final String MANAGER_TYPE_CDI = "cdi";

    public static Vertx pmbVertx;
    public static JsonObject pmbConfig;

    @Override
    public void start() throws Exception {
        super.start();

        DB_SCHEMA = config.getString("db-schema");
        SCHOOL_TABLE = DB_SCHEMA + ".etablissement";

        pmbVertx = vertx;
        pmbConfig = config;

        JsonObject exportConfig = config.getJsonObject("export");
        JsonObject PMBConfig = config.getJsonObject("PMB");
        PMBServer.getInstance().init(vertx, PMBConfig);
        EmailFactory emailFactory = new EmailFactory(vertx, config);
        EmailSender emailSender = emailFactory.getSender();

        addController(new PmbController(exportConfig));
        addController(new SchoolController());
        addController(new EmailSendController(emailSender, config.getString("infraMail", null)));
    }

}
