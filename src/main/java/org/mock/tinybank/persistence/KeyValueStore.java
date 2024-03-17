package org.mock.tinybank.persistence;

import java.util.Hashtable;

public class KeyValueStore<K,V> {
    private final Hashtable<K,V> hashtable;

    public KeyValueStore() {
        this.hashtable = new Hashtable<>();
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
}
