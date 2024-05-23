package fr.openent.pmb.helper;

import fr.wseduc.webutils.Either;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.*;
import io.vertx.core.http.impl.headers.HeadersMultiMap;
import io.vertx.core.net.ProxyOptions;
import org.entcore.common.controller.ControllerHelper;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicBoolean;

import static fr.openent.pmb.Pmb.pmbConfig;
import static fr.openent.pmb.Pmb.pmbVertx;

public class HttpClientHelper extends ControllerHelper {


    public HttpClientHelper() {
        super();
    }

    /**
     * Create default HttpClient
     * @return new HttpClient
     */
    public static HttpClient createHttpClient(Vertx vertx) {
        boolean setSsl = true;
        try {
            setSsl = "https".equals(new URI(pmbConfig.getString("host")).getScheme());
        } catch (URISyntaxException e) {
            log.error("Invalid pmb uri",e);
        }
        final HttpClientOptions options = new HttpClientOptions();
        options.setSsl(setSsl);
        options.setTrustAll(true);
        options.setVerifyHost(false);
        if (System.getProperty("httpclient.proxyHost") != null) {
            ProxyOptions proxyOptions = new ProxyOptions()
                    .setHost(System.getProperty("httpclient.proxyHost"))
                    .setPort(Integer.parseInt(System.getProperty("httpclient.proxyPort")))
                    .setUsername(System.getProperty("httpclient.proxyUsername"))
                    .setPassword(System.getProperty("httpclient.proxyPassword"));
            options.setProxyOptions(proxyOptions);
        }
        int maxPoolSize = pmbConfig.getInteger("http-client-max-pool-size", 0);
        if(maxPoolSize > 0) {
            options.setMaxPoolSize(maxPoolSize);
        }
        return vertx.createHttpClient(options);
    }

    public static void webServicePmbGet(String pmbUrl, HttpServerRequest request,
            Handler<Either<String, Buffer>> handler) throws UnsupportedEncodingException {

        final AtomicBoolean responseIsSent = new AtomicBoolean(false);
        final HttpClient httpClient = HttpClientHelper.createHttpClient(pmbVertx);

        URI url;
        try {
            url = new URI(pmbUrl);
        } catch (URISyntaxException e) {
            handler.handle(new Either.Left<>("Bad request"));
            return;
        }

        RequestOptions requestOptions = new RequestOptions()
                .setAbsoluteURI(url.toString())
                .setMethod(HttpMethod.GET)
                .setHeaders(new HeadersMultiMap());

        for(String key : request.headers().names()){
            requestOptions.putHeader(key, request.getHeader(key));
        }
        requestOptions.putHeader("Host", pmbConfig.getString("header"));
        requestOptions.putHeader("Content-type", "application/x-www-form-urlencoded");


        httpClient.request(requestOptions)
                .flatMap(clientRequest ->  {
                    clientRequest.setFollowRedirects(true);
                    return clientRequest.send();
                })
                .onSuccess(response -> {
                    if (response.statusCode() != 200) {
                        log.error("Fail to get webservice" + response.statusMessage());
                        response.bodyHandler(event -> {
                            log.error("Returning body after GET CALL : " + pmbUrl + ", Returning body : " + event.toString("UTF-8"));
                            handler.handle(new Either.Left<>("Returning body after GET CALL : " + pmbUrl + ", Returning body : " + event.toString("UTF-8")));
                            if (!responseIsSent.getAndSet(true)) {
                                httpClient.close();
                            }
                        });
                    }

                    final Buffer body = Buffer.buffer();
                    response.handler(body::appendBuffer);
                    response.endHandler(end -> {
                        handler.handle(new Either.Right<>(body));
                        if (!responseIsSent.getAndSet(true)) {
                            httpClient.close();
                        }
                    });
                })
                .onFailure(throwable -> {
                    log.error(throwable.getMessage(), throwable);
                    if (!responseIsSent.getAndSet(true)) {
                        httpClient.close();
                    }
                });

    }
}
