import app.AppConfig;
import app.ServentInfo;
import app.kademlia.RoutingTable;
import app.kademlia.RoutingTableImpl;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestClass {
    @Test
    public void test1() {
        List<Integer> myList = new ArrayList<>();
        myList.add(7);
        myList.add(0, 8);
        myList.add(0, 10);
        System.out.println("Hello: " + myList);
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
}
