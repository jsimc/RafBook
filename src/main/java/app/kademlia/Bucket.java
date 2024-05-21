package app.kademlia;

import app.ServentInfo;

import java.io.Serializable;
import java.util.List;

public interface Bucket extends Serializable {
    int getId();
    int size();
    boolean contains(int id);
    boolean contains(ServentInfo servent); // node
    void add(ServentInfo newServent);

    void remove(ServentInfo servent);
    void remove(int id);
    ServentInfo getNode(int id);
    List<Integer> getNodeIds();
//    static void addToAnswer()
}
