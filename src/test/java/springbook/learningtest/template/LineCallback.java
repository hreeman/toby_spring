package springbook.learningtest.template;

public interface LineCallback<T> {
    T doSomethingWithLine(final String line, final T value);
}
