package com.book.tobyspring.template;

public interface LineCallback<T> {
    T doSomethingWithLine(String line, T value);

}
