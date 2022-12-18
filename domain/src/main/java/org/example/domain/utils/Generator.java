package org.example.domain.utils;

/**
 * The way to provide identifiers to entities. It intended to appear in constructors only, so the lifecycle of beans
 * those implemented Generator<T> is still controlled by Spring, not by ORM library and the risks of memory leaks are avoided
 * @param <T>
 */
public interface Generator<T> {
    T generate();
}
