package com.zzqfsy.dao;

import com.zzqfsy.mongodb.MongoDbClient;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.sync.Sync;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john on 16-6-17.
 */
public class MongoDbDaoImpl<T> implements MongoDbDao<T> {

    @Override
    public T store(String collection, final T entity) {
        final JsonObject jsonEntity = new JsonObject(Json.encode(entity));
        final String id = Sync.awaitResult(handler -> MongoDbClient.getClient().save(collection, jsonEntity, handler));
        return entity;
    }

    @Override
    public T find(String collection, final T entity) {
        final JsonObject findQuery = new JsonObject(Json.encode(entity));
        final JsonObject foundEntity = Sync.awaitResult(handler -> MongoDbClient.getClient().findOne(
            collection,
            findQuery,
            null,
            handler
        ));
        T foundModel = null;

        if (foundEntity != null) {
            System.out.println("Entity was found:  " + foundEntity);
            final String id = foundEntity.getString("_id");
            foundModel = Json.decodeValue(
                foundEntity.encode(),
                (Class<T>) ((ParameterizedType) this.getClass()
                                                    .getGenericSuperclass()).getActualTypeArguments()[0]
            );
        }
        return foundModel;
    }

    @Override
    public List<T> findAll(String collection) {
        final JsonObject query = new JsonObject();
        final List<T> allTodos = new ArrayList<>();
        final List<JsonObject> foundEntities = Sync.awaitResult(handler -> MongoDbClient.getClient().find(
            collection,
            query,
            handler
        ));

        foundEntities.forEach(entity -> {
            final T foundTodoItem = Json.decodeValue(
                entity.encode(),
                (Class<T>) ((ParameterizedType) this.getClass()
                                                    .getGenericSuperclass()).getActualTypeArguments()[0]
            );
            final String id = entity.getString("_id");
            allTodos.add(foundTodoItem);
        });

        return allTodos;
    }

    @Override
    public void delete(String collection, final T entity) {
        final JsonObject deleteQuery = new JsonObject(Json.encode(entity));
        MongoDbClient.getClient().remove(collection, deleteQuery, resultHandler -> {
            final boolean succeeded = resultHandler.succeeded();
            if (succeeded) {
                System.out.println("Removed items matched by the query: " + deleteQuery);
            } else {
                resultHandler.cause().printStackTrace();
            }
        });
    }

    @Override
    public void deleteAll(String collection) {
        MongoDbClient.getClient().remove(collection, new JsonObject(), handler -> {
            final boolean succeeded = handler.succeeded();
            if (succeeded) {
                System.out.println("Removed all items!");
            } else {
                handler.cause().printStackTrace();
            }
        });
    }
}

