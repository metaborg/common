package mb.common.util;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * A map where keys can map to multiple values.
 *
 * @param <K> The type of keys in this collection.
 * @param <V> The type of values in this collection.
 */
public class MultiMap<K, V> implements Serializable {
    private final Map<K, ArrayList<V>> map;
    private final Function<Map<? extends K, ArrayList<V>>, Map<K, ArrayList<V>>> mapFunction;


    public MultiMap(Map<? extends K, ArrayList<V>> map, Function<Map<? extends K, ArrayList<V>>, Map<K, ArrayList<V>>> mapFunction) {
        this.map = mapFunction.apply(map);
        this.mapFunction = mapFunction;
    }

    public MultiMap(MultiMap<K, V> map, Function<Map<? extends K, ArrayList<V>>, Map<K, ArrayList<V>>> mapFunction) {
        this(map.map, mapFunction);
    }

    public MultiMap(MultiMap<K, V> map) {
        this(map.map, map.mapFunction);
    }

    public MultiMap(Function<Map<? extends K, ArrayList<V>>, Map<K, ArrayList<V>>> mapFunction) {
        this(Collections.emptyMap(), mapFunction);
    }


    public static <K, V> MultiMap<K, V> withHash() {
        return new MultiMap<K, V>(new HashMapFunction<>());
    }

    public static <K, V> MultiMap<K, V> withConcurrentHash() {
        return new MultiMap<>(new ConcurrentHashMapFunction<>());
    }

    public static <K, V> MultiMap<K, V> withLinkedHash() {
        return new MultiMap<>(new LinkedHashMapFunction<>());
    }

    public static <K, V> MultiMap<K, V> copyOfWithHash(MultiMap<K, V> multiMap) {
        return new MultiMap<>(multiMap, new HashMapFunction<>());
    }

    public static <K, V> MultiMap<K, V> copyOfWithConcurrentHash(MultiMap<K, V> multiMap) {
        return new MultiMap<>(multiMap, new ConcurrentHashMapFunction<>());
    }

    public static <K, V> MultiMap<K, V> copyOfWithLinkedHash(MultiMap<K, V> multiMap) {
        return new MultiMap<>(multiMap, new LinkedHashMapFunction<>());
    }

    public static <K, V> MultiMap<K, V> copyOfWithHash(Map<? extends K, ArrayList<V>> map) {
        return new MultiMap<>(map, new HashMapFunction<>());
    }

    public static <K, V> MultiMap<K, V> copyOfWithConcurrentHash(Map<? extends K, ArrayList<V>> map) {
        return new MultiMap<>(map, new ConcurrentHashMapFunction<>());
    }

    public static <K, V> MultiMap<K, V> copyOfWithLinkedHash(Map<? extends K, ArrayList<V>> map) {
        return new MultiMap<>(map, new LinkedHashMapFunction<>());
    }


    public int size() {
        return map.values().stream().mapToInt(ArrayList::size).sum();
    }

    public boolean isEmpty() {
        return map.size() == 0;
    }

    public ArrayList<V> get(K key) {
        return map.computeIfAbsent(key, k -> new ArrayList<>());
    }

    public boolean containsKey(K key) {
        return map.containsKey(key);
    }

    public void put(K key, V value) {
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }

    public void putAll(K key, Collection<? extends V> values) {
        map.computeIfAbsent(key, k -> new ArrayList<>()).addAll(values);
    }

    public void putAll(K key, Iterable<? extends V> values) {
        final ArrayList<V> list = map.computeIfAbsent(key, k -> new ArrayList<>());
        IterableUtil.addAll(list, values);
    }

    public void putAll(Map<? extends K, ? extends Collection<? extends V>> mapping) {
        for(Entry<? extends K, ? extends Collection<? extends V>> entry : mapping.entrySet()) {
            final K key = entry.getKey();
            final Collection<? extends V> values = entry.getValue();
            putAll(key, values);
        }
    }

    public void putAll(MultiMap<K, V> mapping) {
        putAll(mapping.map);
    }

    public void replaceAll(K key, ArrayList<V> values) {
        map.put(key, values);
    }

    public void replaceAll(Map<? extends K, ArrayList<V>> mapping) {
        for(Entry<? extends K, ArrayList<V>> entry : mapping.entrySet()) {
            final K key = entry.getKey();
            final ArrayList<V> values = entry.getValue();
            replaceAll(key, values);
        }
    }

    public void replaceAll(MultiMap<K, V> mapping) {
        replaceAll(mapping.map);
    }

    public void removeAll(K key) {
        map.remove(key);
    }

    public void clear() {
        map.clear();
    }

    public Set<K> keySet() {
        return map.keySet();
    }

    public Collection<ArrayList<V>> values() {
        return map.values();
    }

    public Set<Entry<K, ArrayList<V>>> entrySet() {
        return map.entrySet();
    }


    public Map<K, ArrayList<V>> getInnerMap() {
        return map;
    }


    public void forEach(BiConsumer<? super K, ? super ArrayList<V>> action) {
        for(Entry<K, ArrayList<V>> entry : map.entrySet()) {
            action.accept(entry.getKey(), entry.getValue());
        }
    }

    public void forEachValue(BiConsumer<? super K, ? super V> action) {
        for(Entry<K, ArrayList<V>> entry : map.entrySet()) {
            final K key = entry.getKey();
            final ArrayList<V> values = entry.getValue();
            for(V value : values) {
                action.accept(key, value);
            }
        }
    }


    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final MultiMap<?, ?> multiMap = (MultiMap<?, ?>)o;
        if(!map.equals(multiMap.map)) return false;
        return mapFunction.equals(multiMap.mapFunction);
    }

    @Override public int hashCode() {
        int result = map.hashCode();
        result = 31 * result + mapFunction.hashCode();
        return result;
    }

    @Override public String toString() {
        return "MultiMap{" +
            "map=" + map +
            ", mapFunction=" + mapFunction +
            '}';
    }


    private static class HashMapFunction<K, V> implements Function<Map<? extends K, ArrayList<V>>, Map<K, ArrayList<V>>>, Serializable {
        @Override public Map<K, ArrayList<V>> apply(Map<? extends K, ArrayList<V>> m) {
            return new HashMap<>(m);
        }

        @Override public boolean equals(@Nullable Object other) {
            return this == other || other != null && this.getClass() == other.getClass();
        }

        @Override public int hashCode() { return 0; }

        @Override public String toString() { return getClass().getSimpleName(); }
    }

    private static class LinkedHashMapFunction<K, V> implements Function<Map<? extends K, ArrayList<V>>, Map<K, ArrayList<V>>>, Serializable {
        @Override public Map<K, ArrayList<V>> apply(Map<? extends K, ArrayList<V>> m) {
            return new LinkedHashMap<>(m);
        }

        @Override public boolean equals(@Nullable Object other) {
            return this == other || other != null && this.getClass() == other.getClass();
        }

        @Override public int hashCode() { return 0; }

        @Override public String toString() { return getClass().getSimpleName(); }
    }

    private static class ConcurrentHashMapFunction<K, V> implements Function<Map<? extends K, ArrayList<V>>, Map<K, ArrayList<V>>>, Serializable {
        @Override public Map<K, ArrayList<V>> apply(Map<? extends K, ArrayList<V>> m) {
            return new ConcurrentHashMap<>(m);
        }

        @Override public boolean equals(@Nullable Object other) {
            return this == other || other != null && this.getClass() == other.getClass();
        }

        @Override public int hashCode() { return 0; }

        @Override public String toString() { return getClass().getSimpleName(); }
    }
}
