package edu.csun.comp182;

public class Main {
    public static void main(String[] args) {

        MyTreeMap<Integer, String> myMap = new MyTreeMap<>();

        myMap.put(1, "ninety seven");
        myMap.put(2, "automobile");
        myMap.put(3, "eighty nine");
        myMap.put(4, "seventy five");
        System.out.println(myMap.size());

        System.out.println(myMap.isEmpty());
        System.out.println(myMap.get(1));

        System.out.println(myMap.containsKey(1));

        System.out.println(myMap.containsValue("kklk"));
        System.out.println(myMap.containsValue("another string"));

        System.out.println(myMap.keys());
        System.out.println(myMap.values());

        myMap.remove(1);
        myMap.remove(2);
        System.out.println(myMap.size());
        myMap.remove(3);



        System.out.println(myMap.values());
        System.out.println(myMap.keys());

    }
}