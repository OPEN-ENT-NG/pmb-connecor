package fr.openent.pmb.controllers;

import fr.openent.pmb.services.EmailSendService;
import fr.openent.pmb.services.impl.DefaultEmailSendService;
import fr.wseduc.rs.ApiDoc;
import fr.wseduc.rs.Post;
import fr.wseduc.security.ActionType;
import fr.wseduc.security.SecuredAction;
import fr.wseduc.webutils.email.EmailSender;
import fr.wseduc.webutils.http.Renders;
import fr.wseduc.webutils.request.RequestUtils;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import org.entcore.common.controller.ControllerHelper;
import org.entcore.common.http.filter.ResourceFilter;
import org.entcore.common.http.filter.SuperAdminFilter;

public class EmailSendController extends ControllerHelper {
    private static final Logger log = LoggerFactory.getLogger(EmailSendController.class);
    private final EmailSendService emailSendService;
    private final String recipient;

    public EmailSendController(EmailSender emailSender, String recipient) {
        super();
        this.emailSendService = new DefaultEmailSendService(emailSender);
        this.recipient = recipient;
    }


    @Post("/email/send")
    @ApiDoc("Send a mail to infra")
    @SecuredAction(value = "", type = ActionType.RESOURCE)
    @ResourceFilter(SuperAdminFilter.class)
    public void send(HttpServerRequest request) {
        if (recipient != null) {
            RequestUtils.bodyToJsonArray(request, schools -> {
                emailSendService.send(schools, recipient, request);
            });
        } else {
            log.error("Recipient's mail address not found.");
            Renders.renderError(request);
        }
    }

}