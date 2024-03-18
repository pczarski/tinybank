package org.mock.tinybank.persistence;

import java.util.concurrent.ConcurrentHashMap;

public class KeyValueStore<K,V> {
    private final ConcurrentHashMap<K, V> hashtable;

    public KeyValueStore() {
        this.hashtable = new ConcurrentHashMap<>();
    }

    public V put(K key, V value) {
        hashtable.put(key, value);
        return hashtable.get(key);
    }

    public V get(K key) throws EntityNotFoundException {
        V value = hashtable.get(key);
        if(null == value) {
            throw new EntityNotFoundException();
        }
        return value;
    }

    public V delete(K key) {
        return hashtable.remove(key);
    }
}
