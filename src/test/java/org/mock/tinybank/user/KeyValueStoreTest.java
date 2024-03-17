package org.mock.tinybank.user;

import org.junit.jupiter.api.Test;
import org.mock.tinybank.persistence.EntityNotFoundException;
import org.mock.tinybank.persistence.KeyValueStore;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class KeyValueStoreTest {
    @Test
    void put() {
        KeyValueStore<String, Integer> keyValueStore = new KeyValueStore<>();
        assertThat(keyValueStore.put("one", 1)).isEqualTo(1);
        assertThat(keyValueStore.put("two", 2)).isEqualTo(2);
    }

    @Test
    void get() throws EntityNotFoundException {
        KeyValueStore<String, Integer> keyValueStore = new KeyValueStore<>();
        keyValueStore.put("one", 1);
        keyValueStore.put("two", 2);
        assertThat(keyValueStore.get("one")).isEqualTo(1);
        assertThat(keyValueStore.get("two")).isEqualTo(2);
    }

    @Test
    void getKeyDoesNotExists() {
        KeyValueStore<String, Integer> keyValueStore = new KeyValueStore<>();
        assertThatExceptionOfType(EntityNotFoundException.class)
                .isThrownBy(() -> keyValueStore.get("non_existent"));
    }
}