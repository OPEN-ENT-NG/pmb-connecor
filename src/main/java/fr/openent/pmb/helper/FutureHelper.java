package fr.openent.pmb.helper;

import fr.wseduc.webutils.Either;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

public class FutureHelper {
    private static final Logger log = LoggerFactory.getLogger(FutureHelper.class);


    private FutureHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static Handler<Either<String, JsonArray>> handlerJsonArray(Promise<JsonArray> promise) {
        return event -> {
            if (event.isRight()) {
                promise.complete(event.right().getValue());
            } else {
                String message = String.format("[Pmb-connector@%s::handlerJsonArray]: %s",
                        FutureHelper.class.getSimpleName(), event.left().getValue());
                log.error(message);
                promise.fail(event.left().getValue());
            }
        };
    }
}
