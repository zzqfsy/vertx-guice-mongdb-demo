package com.zzqfsy.handle.todo;

import com.zzqfsy.handle.RouteHandle;
import com.zzqfsy.model.mongo.Todo;
import com.zzqfsy.dao.todo.TodoDao;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.RoutingContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by john on 16-6-17.
 */
public class TodoHandle extends RouteHandle implements Handler<RoutingContext> {
    private static final String TODO_COLLECTION_NAME = "todos";

    private final TodoDao todoDao = new TodoDao();

    @Override
    public void handle(RoutingContext routingContext) {
        super.route(routingContext);
    }

    @Override
    public void get(final RoutingContext routingContext){
        final Todo todoToFind = new Todo();
        todoToFind.set_id(routingContext.request().getParam("id"));
        final Todo todo =  todoDao.find(TODO_COLLECTION_NAME, todoToFind);
        final boolean isRoutingContextFailed = routingContext.failed();
        if (!isRoutingContextFailed) {
            final String todoItemAsJson = Json.encode(todo);
            routingContext.response().end(todoItemAsJson);
        }
    }

    @Override
    public void getAll(final RoutingContext routingContext){
        final List<Todo> allTodoItems =  todoDao.findAll(TODO_COLLECTION_NAME);

        final String allTodoItemsAsJson = Json.encode(allTodoItems);
        final HttpServerResponse response = routingContext.response();
        response.end(allTodoItemsAsJson);
    }

    @Override
    public void create(final RoutingContext routingContext){
        final String bodyAsString = routingContext.getBodyAsString();
        final Todo todo = Json.decodeValue(bodyAsString, Todo.class);
        final Todo storedTodo =  todoDao.store(TODO_COLLECTION_NAME, todo);

        final String storedTodoAsJson = Json.encode(storedTodo);
        routingContext.response().end(storedTodoAsJson);
    }

    @Override
    public void update(final RoutingContext routingContext){
        final String bodyAsString = routingContext.getBodyAsString();
        final Todo todo = Json.decodeValue(bodyAsString, Todo.class);
        todoDao.store(TODO_COLLECTION_NAME, todo);

        routingContext.response().end(Json.encode(todo));
    }

    @Override
    public void delete(final RoutingContext routingContext){
        final String idParameter = routingContext.request().getParam("id");
        final Todo todoToFind = new Todo();
        todoToFind.set_id(idParameter);
        todoDao.delete(TODO_COLLECTION_NAME, todoToFind);

        final int noContentStatusCode = HttpStatus.NO_CONTENT.value();
        routingContext.response().setStatusCode(noContentStatusCode).end();
    }

    @Override
    public void deleteAll(final RoutingContext routingContext){
        todoDao.deleteAll(TODO_COLLECTION_NAME);
    }
}
