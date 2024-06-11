package app.kademlia;

import app.MyFile;
import app.ServentInfo;
import app.exceptions.FullBucketException;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface RoutingTable extends Serializable {
    int getNodePrefix(int id);
    Bucket findBucket(int id);
    Bucket findCacheBucket(int id);
    int update(ServentInfo servent); // FullBucketException
    void updateCache(ServentInfo servent);
    void softUpdate(ServentInfo serventInfo);
    void delete(ServentInfo servent);
    FindNodeAnswer findClosest(int destinationId);
    boolean contains(int id);
    List<Bucket> getBuckets();
    int getDistance(int id);
    Map<Integer, MyFile> getValueMap();
    void addToMyFiles(MyFile value);
    List<MyFile> getAllMyFiles();
    List<MyFile> getPublicMyFiles();
    void putValue(int key, MyFile value);
    MyFile getValue(int key);
    MyFile removeValue(int key);
    boolean containsValue(int key);
}
