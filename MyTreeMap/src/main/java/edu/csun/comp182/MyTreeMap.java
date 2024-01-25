package edu.csun.comp182;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class MyTreeMap  <K extends Comparable<K>, V>
        implements MyMap<K,V>{
    private Node<K, V> root;

    private int size;

    public MyTreeMap(){
        root = null;
        size= 0;
    }

    public int size(){
        return size;
    }

    public void clear(){
        root = null;
        size = 0;
    }

    public boolean containsKey(K key){
        if (root == null)
            return false;
        return root.nodeContainsKey(key);
    }

    @Override
    public boolean containsValue(V value) {
        if (root == null)
            return false;
        return root.nodeCanFindValue(value);
    }

    @Override
    public boolean isEmpty(){
        return size == 0;
    }

    public V put(K key, V value){
        if (isEmpty()) {//there is nothing here yet
            root = new Node<>(key, value);
            size = 1;
        } else {
            AtomicBoolean addedSomething = new AtomicBoolean(false);
            V original = root.putAndReportIfAdded(key, value, addedSomething);

            if (addedSomething.get())
                size++;
            return original;
        }
        return value;
    }

    @Override
    public V get(K key) {
        if (root == null)
            return null;
        return root.nodeGet(key);
    }

    @Override
    public V remove(K key) {
        if (root == null)
            return null;

        AtomicBoolean somethingRemoved = new AtomicBoolean(false);
        V returnValue = root.removeMap(key, somethingRemoved);
        if (somethingRemoved.get()){
            size--;
            if (size == 0)
                root = null;
        }
        return returnValue;
    }

    @Override
    public Collection<V> values() {
        ArrayList <V> results = new ArrayList<>();
        if (root != null)
            root.addAllValues(results);

        return results;
    }
    public Set<K> keys() {
        HashSet <K> results = new HashSet<>();
        if (root != null)
            root.addAllKeys(results);

        return results;
    }


}


