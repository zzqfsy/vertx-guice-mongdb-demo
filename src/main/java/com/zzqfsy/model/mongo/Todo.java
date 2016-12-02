package com.zzqfsy.model.mongo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.base.MoreObjects;

/**
 * A class that represents a single Todo item in the system.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Todo extends BaseModel {

    private String _id;
    private String url;
    private String title;
    private Integer order;
    private Boolean completed;

    /**
     * Merges the current item with a new one.
     * <p>
     * Title, order, completed properties are updated if new values are non-null.
     *
     * @param newTodo a new item with updated properties.
     */
    public void merge(Todo newTodo) {
        final String newTitle = newTodo.getTitle();
        final Integer newOrder = newTodo.getOrder();
        final Boolean newCompleted = newTodo.isCompleted();
        if (newTitle != null) {
            title = newTitle;
        }
        if (newOrder != null) {
            order = newOrder;
        }
        if (newCompleted != null) {
            this.completed = newCompleted;
        }
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Boolean isCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("_id", _id)
                          .add("url", url)
                          .add("title", title)
                          .add("order", order)
                          .add("completed", completed)
                          .toString();
    }
}