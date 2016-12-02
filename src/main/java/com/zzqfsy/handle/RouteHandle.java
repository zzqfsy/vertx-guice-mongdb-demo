package com.zzqfsy.handle;

import co.paralleluniverse.fibers.Suspendable;
import com.zzqfsy.model.mongo.BaseModel;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.ext.web.RoutingContext;

import java.util.regex.Pattern;

/**
 * Created by zzqana on 6/21/2016.
 */
public abstract class RouteHandle {
    private final static String pathPattern = "/\\w*/\\w*/\\w*/\\w*";

    @Suspendable
    protected void route(final RoutingContext routingContext) {
        Pattern p = Pattern.compile(pathPattern);
        if (routingContext.request().method() == HttpMethod.GET){
            if (p.matcher(routingContext.request().path()).matches()){
                get(routingContext);
            }else{
                getAll(routingContext);
            }
        }else if (routingContext.request().method() == HttpMethod.POST){
            create(routingContext);
        }else if (routingContext.request().method() == HttpMethod.PATCH){
            update(routingContext);
        }else if (routingContext.request().method() == HttpMethod.DELETE){
            if (p.matcher(routingContext.request().path()).matches()){
                delete(routingContext);
            }else{
                deleteAll(routingContext);
            }
        }
    }

    protected abstract void get(final RoutingContext routingContext);
    protected abstract void getAll(final RoutingContext routingContext);
    protected abstract void create(final RoutingContext routingContext);
    protected abstract void update(final RoutingContext routingContext);
    protected abstract void delete(final RoutingContext routingContext);
    protected abstract void deleteAll(final RoutingContext routingContext);
}
