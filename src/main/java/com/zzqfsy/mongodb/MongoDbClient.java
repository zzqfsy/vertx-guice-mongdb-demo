package com.zzqfsy.mongodb;

import com.sun.org.apache.bcel.internal.generic.RET;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

import java.awt.image.VolatileImage;

/**
 * Created by john on 16-6-17.
 */
public class MongoDbClient {
    private static MongoClient mongoClient;

    private MongoDbClient(final Vertx vertx) {
        final JsonObject mongoConfig = getMongoConfig();
        mongoClient = MongoClient.createShared(vertx, mongoConfig);
    }

    public static MongoClient ClientFactory(final Vertx vertx){
        if (mongoClient == null)
            new MongoDbClient(vertx);

        return mongoClient;
    }

    public static MongoClient getClient(){
        return mongoClient;
    }

    public static void disconnect(){
        if (mongoClient != null){
            mongoClient.close();
        }
    }

    private JsonObject getMongoConfig() {
        String dbUrl = System.getenv("OPENSHIFT_MONGODB_DB_URL");
        if (dbUrl == null) {
            dbUrl = "mongodb://localhost:27017";
        }
        return new JsonObject()
            .put("connection_string", dbUrl)
            //.put("replicaSet", dbUrl)
            .put("db_name", "test");
    }

}
