package ch.grignola.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

public class DistinctByKey<T> {
    private final Function<T, Object> function;
    private final Set<Object> seenObjects;

    public DistinctByKey(Function<T, Object> function) {
        this.function = function;
        this.seenObjects = new HashSet<>();
    }

    public boolean filterByKey(T t) {
        return seenObjects.add(function.apply(t));
    }
}