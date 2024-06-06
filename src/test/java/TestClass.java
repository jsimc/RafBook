import app.AppConfig;
import app.ServentInfo;
import app.kademlia.DistanceComparator;
import app.kademlia.RoutingTable;
import app.kademlia.RoutingTableImpl;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;

public class TestClass {
    @Test
    public void test1() {
        List<Integer> myList = new ArrayList<>();
        myList.add(7);
        myList.add(0, 8);
        myList.add(0, 10);
        System.out.println("List: " + myList);
        myList.remove(myList.size()-1);
        System.out.println("List: " + myList);
//        for (int i = 0; i < myList.size(); i++) {
//            System.out.println(myList.get(i));
//        }
    }

    @Test
    public void test2() {
        RoutingTable routingTable = new RoutingTableImpl();
        AppConfig.myServentInfo = new ServentInfo(11, "localhost", 1670);

//        int id = 5;
        routingTable.update(new ServentInfo(5, "localhost", 1100));
        routingTable.update(new ServentInfo(6, "localhost", 1200));
        routingTable.update(new ServentInfo(7, "localhost", 1300));
        routingTable.update(new ServentInfo(8, "localhost", 1500));
        routingTable.update(new ServentInfo(9, "localhost", 1670));
        routingTable.update(new ServentInfo(10, "localhost", 1671));
//        int lastPrefix = routingTable.getNodePrefix(6);
//        System.out.println(Integer.toBinaryString(lastPrefix));
        routingTable.findClosest(46);
    }

    @Test
    public void testDistanceComparator() {
        int key = 5;
        DistanceComparator comparator = new DistanceComparator(key);
        Set<ServentInfo> lista = new LinkedHashSet<>();
        lista.add(new ServentInfo(6, "localhost", 1200));
        lista.add(new ServentInfo(7, "localhost", 1300));
        lista.add(new ServentInfo(8, "localhost", 1500));
        lista.add(new ServentInfo(9, "localhost", 1670));
        System.out.println(lista);

        lista = lista.stream().sorted(comparator).collect(Collectors.toCollection(LinkedHashSet::new));

        System.out.println(lista);
    }

    @Test
    public void test3() {
        List<Integer> lista = new ArrayList<>();
//        lista.add(1234);
//        System.out.println(lista.get(0));

        String proba = "jelena";
        System.out.println(Math.abs(proba.hashCode()));
        System.out.println(valueHash(proba));
//        System.out.println(Math.abs(proba.hashCode())%64);
//        int hash = (proba.hashCode()%64) & 0xffffff;
//        System.out.println(hash);
    }

    @Test
    public void hashSetTest() {
        // Initial Set
        Set<String> set = new HashSet<>();
        set.add("Apple");
        set.add("Banana");
        set.add("Cherry");

        // Initial List
        List<String> list = new ArrayList<>();
        list.add("Banana");
        list.add("Date");
        list.add("Elderberry");

        // New Set to hold merged elements
        Set<String> mergedSet = new HashSet<>(set);

        // Add all elements from the list to the new set
        mergedSet.addAll(list);

        // Print the merged set
        for (String element : mergedSet) {
            System.out.println(element);
        }
    }

    private int hash(int value) {
        return Math.abs(61 * value) % (int) Math.pow(2, 6);
    }

    private int valueHash(String value) {
        return hash(value.hashCode());
    }
}
