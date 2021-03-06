package com.jean.database.api;

public interface IMouseEventHandler<T> {

    void onClick(T t);

    void onDoubleClick(T t);

    void onSelected(T t);

}
