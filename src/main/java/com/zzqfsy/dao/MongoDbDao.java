package com.zzqfsy.dao;

import co.paralleluniverse.fibers.Suspendable;

import java.util.List;

/**
 * Created by john on 16-6-17.
 */
public interface MongoDbDao<T> {

    @Suspendable
    T store(String collection, T entity);

    @Suspendable
    T find(String collection, T entity);

    @Suspendable
    List<T> findAll(String collection);

    void delete(String collection, T entity);

    void deleteAll(String collection);
}