package fr.openent.pmb.server;

import fr.openent.pmb.bean.Credential;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.net.ProxyOptions;
import io.vertx.core.net.ProxyType;

import java.net.URI;
import java.net.URISyntaxException;

public class PMBServer {
    private Logger log = LoggerFactory.getLogger(PMBServer.class);

    private Credential credential;
    private String dbPrefix;
    private String endpoint;
    private HttpClient httpClient;
    private String host;
    private int pageSize;

    private PMBServer() {
    }

    public static PMBServer getInstance() {
        return PMBServerHolder.instance;
    }

    public void init(Vertx vertx, JsonObject config) {
        if (!config.containsKey("host") || !config.containsKey("endpoint") || !config.containsKey("credentials") || config.getJsonObject("credentials", new JsonObject()).isEmpty()) {
            throw new RuntimeException("Unable to init PMB server instance. Please fill PMB configuration");
        }

        this.host = config.getString("host");
        this.endpoint = config.getString("endpoint");
        this.dbPrefix = config.getString("db_prefix", "");
        this.pageSize = config.getInteger("page_size", 200);
        JsonObject credentials = config.getJsonObject("credentials");
        this.credential = new Credential(credentials.getString("username"), credentials.getString("password"));
        initHttpClient(vertx);
    }

    private void initHttpClient(Vertx vertx) {
        try {
            final HttpClientOptions options = new HttpClientOptions();

            URI uri = new URI(host);
            options.setDefaultHost(host)
                    .setDefaultPort("https".equals(uri.getScheme()) ? 433 : 80)
                    .setSsl("https".equals(uri.getScheme()))
                    .setKeepAlive(true)
                    .setVerifyHost(false)
                    .setTrustAll(true);

            if (System.getProperty("httpclient.proxyHost") != null) {
                ProxyOptions proxyOptions = new ProxyOptions()
                        .setType(ProxyType.HTTP)
                        .setHost(System.getProperty("httpclient.proxyHost"))
                        .setPort(Integer.parseInt(System.getProperty("httpclient.proxyPort")));
                options.setProxyOptions(proxyOptions);
            }

            this.httpClient = vertx.createHttpClient(options);
        } catch (URISyntaxException | NumberFormatException e) {
            log.error("Failed to init http client", e);
        }
    }

    public int pageSize() {
        return this.pageSize;
    }

    private String dbParam(String dbName) {
        return String.format("%s%s", dbPrefix, dbName);
    }

    private String uri(String dbName) {
        String h = endpoint.startsWith("/") ? host : host + "";
        return String.format("%s%s?database=%s", h, endpoint, dbParam(dbName));
    }

    public void request(String uai, JsonObject content, Handler<AsyncResult<JsonObject>> handler) {
        HttpClientRequest req = httpClient.postAbs(this.uri(uai), response -> {
            if (response.statusCode() != 200) {
                handler.handle(Future.failedFuture(response.statusMessage()));
                return;
            }

            Buffer body = Buffer.buffer();
            response.handler(body::appendBuffer);
            response.endHandler(aVoid -> handler.handle(Future.succeededFuture(new JsonObject(new String(body.getBytes())))));
            response.exceptionHandler(throwable -> handler.handle(Future.failedFuture(throwable)));
        });
        req.putHeader(HttpHeaders.CONTENT_TYPE.toString(), "application/json");
        req.putHeader(HttpHeaders.AUTHORIZATION.toString(), String.format("Basic %s", credential.basic()));
        req.end(content.encode());
    }

    public String host() {
        return this.host;
    }

    private static class PMBServerHolder {
        private static final PMBServer instance = new PMBServer();
    }
}
