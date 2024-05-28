package app.kademlia;

import app.ServentInfo;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class BucketImpl implements Bucket {

    private static final long serialVersionUID = -6049123458368168254L;

    private int id;
    private List<Integer> nodeIds;
    private Map<Integer, ServentInfo> nodeMap;


    public BucketImpl(int id) {
        this.id = id;
        nodeIds = new CopyOnWriteArrayList<>();
        nodeMap = new ConcurrentHashMap<>();
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public int size() {
        return this.nodeIds.size();
    }

    @Override
    public boolean contains(int id) {
        return this.nodeIds.contains(id);
    }

    @Override
    public boolean contains(ServentInfo servent) {
        return this.nodeIds.contains(servent.getHashId());
    }

    @Override
    public void add(ServentInfo newServent) {
        // ubacujemo u listu kao LIFO ! {3, 4, 5} --> add 7 --> {7, 3, 4, 5}
        nodeIds.add(0, newServent.getHashId());
        nodeMap.put(newServent.getHashId(), newServent);
    }

    @Override
    public void pushToFront(ServentInfo serventInfo) { // on the front is Most Recently Used, on the back is Least RU
        nodeIds.remove(Integer.valueOf(serventInfo.getHashId()));
        nodeIds.add(0, serventInfo.getHashId());
    }

    @Override
    public void remove(ServentInfo servent) {
        this.remove(servent.getHashId());
    }

    @Override
    public void remove(int id) {
        nodeIds.remove(Integer.valueOf(id)); // object is integer
        nodeMap.remove(id);
    }

    @Override
    public ServentInfo getNode(int id) {
        return nodeMap.get(id);
    }

    @Override
    public List<Integer> getNodeIds() {
        return nodeIds;
    }

    @Override
    public String toString() {
        return "Bucket: id = " + id + " " +
                "nodeIds = " + nodeIds;
    }
}
