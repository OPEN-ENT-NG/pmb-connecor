package fr.openent.pmb.services.impl;

import fr.openent.pmb.services.EmailSendService;
import fr.wseduc.webutils.email.EmailSender;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class DefaultEmailSendService implements EmailSendService {

    private final EmailSender emailSender;

    public DefaultEmailSendService(EmailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void send(JsonArray schools, String recipient, HttpServerRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append("Les établissements suivants ont été supprimés de leur cité scolaire sur Pmb-connector :\n\n");
        for (int i = 0; i < schools.size(); i++) {
            JsonObject school = schools.getJsonObject(i);
            sb.append("- " + school.getString("uai", "") + " " + school.getString("nom", "") + "\n");
        }
        sb.append("Veuillez bien créer une nouvelle base PMB pour chacun d'entre eux.");
        String body = sb.toString();

        emailSender.sendEmail(request,
                recipient,
                null,
                null,
                "Créations de bases PMB nécessaires",
                body,
                null,
                false,
                null);

    }

}
