package edu.csun.comp182;

import java.util.Collection;
import java.util.Set;

public interface MyMap <K extends Comparable <K> , V>{

    void clear();

    int size();

    boolean isEmpty();

    boolean containsKey(K key);  // o(log (n)) *need*

    boolean containsValue(V value);  // *need*

    V put(K key_arg, V value_arg);

    V get(K key);  // *need*

    V remove(K key);    // *need*

    Collection<V> values(); // *need*

    Set<K> keys();  // *need*
}
