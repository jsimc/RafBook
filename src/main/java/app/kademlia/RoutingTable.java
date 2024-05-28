package app.kademlia;

import app.ServentInfo;
import app.exceptions.FullBucketException;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface RoutingTable extends Serializable {
    int getNodePrefix(int id);
    Bucket findBucket(int id);
    int update(ServentInfo servent); // FullBucketException
    void softUpdate(ServentInfo serventInfo);
    void delete(ServentInfo servent);
    FindNodeAnswer findClosest(int destinationId);
    boolean contains(int id);
    List<Bucket> getBuckets();
    int getDistance(int id);
    Map<Integer, String> getValueMap();
    void putValue(int key, String value);
    String getValue(int key);
    void removeValue(int key);
    boolean containsValue(int key);

}
