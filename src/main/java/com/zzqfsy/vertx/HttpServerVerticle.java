package com.zzqfsy.vertx;

import co.paralleluniverse.fibers.Suspendable;
import com.google.common.collect.Sets;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.zzqfsy.guice.HandleModule;
import com.zzqfsy.handle.todo.TodoHandle;
import com.zzqfsy.mongodb.MongoDbClient;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sync.SyncVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static io.vertx.ext.sync.Sync.fiberHandler;

/**
 * Created by john on 16-6-16.
 */
public class HttpServerVerticle extends SyncVerticle {

    public static final String API_BASE_URL = "/api/manager/todos";
    public static final String DOMAIN = "http://api.stlc.cn";
    public static final Integer PORT = Integer.getInteger("http.port", 8081);
    public static final String HOST = System.getProperty("http.address", "localhost");

    private static final String[] CORS_HEADERS = new String[]{HttpHeaders.CONTENT_TYPE};
    private static final HttpMethod[] CORS_METHODS = HttpMethod.values();
    private static final String CORS_ORIGIN = "*";

    private HttpServer httpServer;
    private Router router;

    @Override
    @Suspendable
    public void start(Future<Void> startFuture) throws Exception {

        Injector injector = Guice.createInjector(new HandleModule());

        httpServer = vertx.createHttpServer();
        router = Router.router(vertx);
        final CorsHandler corsHandler = createCorsHandler(CORS_ORIGIN, CORS_HEADERS, CORS_METHODS);
        final BodyHandler bodyHandler = BodyHandler.create();

        //create mongodb connection
        MongoDbClient.ClientFactory(vertx);

        // Attaching a CORS handler for all requests
        router.route().handler(corsHandler);
        // Attaching a body handler for all requests to capture their body
        router.route().handler(bodyHandler);

        // Attaching a root handler to monitor the state of the application
        router.get("/").produces(MediaType.PLAIN_TEXT_UTF_8.toString()).handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response.end("Application is up and running!");
        });


        TodoHandle todoHandle = injector.getInstance(TodoHandle.class);
        router.get(API_BASE_URL).produces(MediaType.JSON_UTF_8.toString()).handler(fiberHandler(todoHandle));
        router.get(API_BASE_URL + "/:id").produces(MediaType.JSON_UTF_8.toString()).handler(fiberHandler(todoHandle));
        router.post(API_BASE_URL).produces(MediaType.JSON_UTF_8.toString()).handler(fiberHandler(todoHandle));
        router.delete(API_BASE_URL).handler(fiberHandler(todoHandle));
        router.delete(API_BASE_URL + "/:id").handler(fiberHandler(todoHandle));
        router.patch(API_BASE_URL + "/:id").consumes(MediaType.JSON_UTF_8.toString()).produces(MediaType.JSON_UTF_8.toString()).handler(fiberHandler(todoHandle));

        startServer(startFuture);
    }

    @Override
    @Suspendable
    public void stop(Future<Void> stopFuture) throws Exception {
        if (httpServer == null) {
            stopFuture.complete();
        } else {
            final Handler<AsyncResult<Void>> completer = stopFuture.completer();
            httpServer.close(completer);
        }
    }

    private void startServer(Future<Void> startFuture) {
        httpServer.requestHandler(router::accept).listen(PORT, HOST, result -> {
            final boolean failedToStart = result.failed();
            if (failedToStart) {
                final Throwable cause = result.cause();
                startFuture.fail(cause);
            } else {
                startFuture.complete();
            }
        });
    }

    private CorsHandler createCorsHandler(final String origin, final String[] headers, final HttpMethod[] methods) {
        final Set<HttpMethod> httpMethodSet = Sets.newHashSet(methods);
        final Set<String> httpHeaderSet = Sets.newHashSet(headers);
        return CorsHandler.create(origin)
                          .allowedHeaders(httpHeaderSet)
                          .allowedMethods(httpMethodSet);
    }
}
