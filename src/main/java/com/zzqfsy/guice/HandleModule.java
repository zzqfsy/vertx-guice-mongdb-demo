package com.zzqfsy.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;
import com.zzqfsy.handle.todo.TodoHandle;

/**
 * Created by john on 16-6-22.
 */
public class HandleModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(TodoHandle.class).in(Scopes.SINGLETON);
    }
}
