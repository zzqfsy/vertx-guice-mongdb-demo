package com.zzqfsy.dao.todo;

import com.zzqfsy.dao.MongoDbDaoImpl;
import com.zzqfsy.model.mongo.Todo;
import org.springframework.stereotype.Component;

/**
 * Created by john on 16-6-17.
 */
public class TodoDao extends MongoDbDaoImpl<Todo> {}
