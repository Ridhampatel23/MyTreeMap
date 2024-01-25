package edu.csun.comp182;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Node <K extends Comparable<K>, V> {
    //K key ==> V value

    private K key;
    private V value;
    private Node<K, V> left;

    private Node<K, V> right;

    public Node(K key_arg, V value_arg) {
        key = key_arg;
        value = value_arg;
        left = null;
        right = null;
    }


    private Node<K, V> findTargetNode(K key_arg) {
        //will return tje node that contains current key "key" ,or
        //the node that would be the parent f a new node with key "key"
        //return value Node<K,V> temp = this;
        Node<K, V> temp = this;

        boolean targetFound = false; // needs initialization
        int direction;

        while (!targetFound) {
            direction = key_arg.compareTo(temp.key);
            if (direction == 0) {
                targetFound = true;
            } else if (direction < 0) {
                if (temp.left == null)
                    targetFound = true;
                else
                    temp = temp.left;
            } else {
                if (temp.right == null)
                    targetFound = true;
                else
                    temp = temp.right;
            }
        }
        return temp;
    }

    public V putAndReportIfAdded(K key_arg, V value_arg, AtomicBoolean added) {
        Node<K, V> targetnode = findTargetNode(key_arg);

        int direction = key_arg.compareTo(targetnode.key);

        if (direction == 0) {
            //replace existing key -> value mapping
            V original = targetnode.value;
            targetnode
                    .value = value_arg;
            added.set(false);
            return original;
        } else if (direction < 0) {
            added.set(true);
            targetnode.left = new Node<>(key_arg, value_arg);
            return null;
        } else {
            added.set(true);
            targetnode.right = new Node<>(key_arg, value_arg);
            return null;

        }

    }

    public boolean nodeContainsKey(K key) {
//        Node<K,V> temp= this;
//
//        while (temp!= null && key.compareTo(temp.key) != 0){
//            if (key.compareTo(temp.key) < 0){
//                temp = temp.left;
//            } else if (key.compareTo(temp.key) > 0) {
//                temp = temp.right;
//
//            }
//        }
//        return temp != null;

        Node<K,V> Node = findKeyNode(key, null);
        return (Node != null);

    }

    private Node<K, V> findKeyNode(K key , AtomicReference<Node<K,V>> parentWrapper) {
        Node<K, V> temp = this;

        while ((temp != null) && key.compareTo(temp.key) != 0) {
            parentWrapper.set(temp);
            if (key.compareTo(temp.key) < 0)
                temp = temp.left;
            else temp = temp.right;
        }
            return temp;

    }

    public V nodeGet(K key) {
        Node<K, V> node = findKeyNode(key, null);

        if (node == null)
            return null;
        else
            return node.value;
    }

    public boolean nodeCanFindValue(V value_arg) {

//        return ((value_arg == null && value == null) ||
//                (value_arg != null && value != null && value_arg.equals(value)) ||
//                (left != null && left.nodeCanFindValue(value_arg)) ||
//                (right != null && right.nodeCanFindValue(value_arg)));

       //if this nodes value and the value i am searching for are both null then ive found it.
        if (value_arg == null && value== null)
            return true;

        // if they both not null values, and the values and equal, then also found it.
        if (value_arg != null && value != null
                  &&  value_arg.equals(value))
            return true;

        if (left != null)  //if there's a left subtree to search... look in there
            if (left.nodeCanFindValue(value_arg)) // if you find it.... then return true;
                return true;

        if (right != null) // if there's a right subtree to search...look in there
            if (right.nodeCanFindValue(value_arg)) // if u find it, return true;
                return true;

        //otherwise...unable to find match here, or in the left subtree, or the right. so it doesnt exist.
        return false;
    }

    public void addAllValues(Collection<V> bag) {
        // this node has a value to keep...
        bag.add(value);
        if (left != null)
            left.addAllValues(bag);
        if (right != null)
            right.addAllValues(bag);
    }

    public void addAllKeys(Set<K> set) {
        // this node has a value to keep...
        set.add(key);
        if (left != null)
            left.addAllKeys(set);
        if (right != null)
            right.addAllKeys(set);
    }

    private void clipOutEasyNode(Node<K, V> node, Node<K, V> parent) {
        //what is the poimter to the remaining child I have
        Node<K, V> remaining_child = node.left;
        if (node.left == null)
            remaining_child = node.right;

        if (parent == null) {
            if (node.right != null) {
                    node.key = remaining_child.key;
                    node.value = remaining_child.value;
                    node.left = remaining_child.left;
                    node.right = remaining_child.right;
                }
            } else {// we do have a parent!!


                if (parent.left == node)// we are the left child
                    parent.left = remaining_child; // migt be null... when no children at all.
                else
                    parent.right = remaining_child;
            }
        }


    private Node<K,V> findPredecessor (AtomicReference<Node<K,V>> parentWrapper){
        Node<K,V> predNode = this;

        if (left != null){
            predNode = left;
            while (predNode.right != null){
                parentWrapper.set(predNode);
                predNode = predNode.right;
            }
        }
        return predNode;
    }

    public V removeMap(K key, AtomicBoolean somethingRemoved) {
        // we know we are the root node
        // step 1: find the node with the desired key (normal search)
        // keep that pointer (and its parent)


        AtomicReference<Node<K,V>> parentWrapper = new AtomicReference<>(null);
       Node<K,V> keyNode = findKeyNode(key,  parentWrapper);
        // do step 1.

        if (keyNode == null) {
            somethingRemoved.set(false);
            return null;
        }
        // step 2 : replace keyNode with the inorder predecessor
        //capture retrun values of the found mapping
        V retValue = keyNode.value;
        somethingRemoved.set(true);

        //find a suitable replacement Node
        Node<K,V> predNode = keyNode.findPredecessor(parentWrapper);

        //swap replacement node contents into existing map node if replacement node is actually diff
        //keynode...mao to "remove", prednode... node to copy contents and actually node removal.
        if (keyNode != predNode){ // copy contents
            keyNode.key = predNode.key;
            keyNode.value = predNode.value;
        }

        //no matter what, physically remove the "replacement node"
             clipOutEasyNode(keyNode, parentWrapper.get());

        return retValue;
        }


    }




