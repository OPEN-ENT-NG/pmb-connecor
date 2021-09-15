package fr.openent.pmb.services;

import fr.wseduc.webutils.Either;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonArray;

public interface EmailSendService {

    /**
     * Send a mail to infra
     * @param schools list of schools deleted from postgre db
     * @param recipient recipient's mail address
     * @param request the http request
     */
    void send(JsonArray schools, String recipient, HttpServerRequest request);

}
