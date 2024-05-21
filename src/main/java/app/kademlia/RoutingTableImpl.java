package app.kademlia;

import app.AppConfig;
import app.ServentInfo;
import app.exceptions.FullBucketException;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class RoutingTableImpl implements RoutingTable {

    private List<Bucket> buckets;
    private Map<Integer, String> valueMap;

    public RoutingTableImpl() {
        this.buckets = new CopyOnWriteArrayList<>();
        for (int i = 0; i <= AppConfig.ID_SIZE; i++) { // 0, 1, 2, 3, 4, 5, 6
            buckets.add(new BucketImpl(i));
        }
    }

    /**
     * proverava prvi bit da li je 0 ili 1, ali prvo shiftuje id tako da zapravo nalazi gde se nalazi prva jedinica na kojoj poziciji
     * @param id
     * @return
     */
    @Override
    public int getNodePrefix(int id) {
        for (int j = 0; j < AppConfig.ID_SIZE; j++) {
            if ((id >> (AppConfig.ID_SIZE - 1 - j) & 0x1) != 0) {
                return AppConfig.ID_SIZE - j;
            }
        }
        return 0;
    }

    @Override
    public Bucket findBucket(int id) {
        int xorNumber = this.getDistance(id);
        int prefix = this.getNodePrefix(xorNumber);
        return buckets.get(prefix);
    }

    /**
     * Returns 0 if servent is added to the bucket, returns -1 if bucket contains servent already,
     * returns -2 if bucket is full and cant add more servents
     * @param servent
     * @return
     */
    @Override
    public int update(ServentInfo servent){
        Bucket bucket = this.findBucket(servent.getHashId()); // nalazimo bucket u koji cemo da ga stavimo.
        if (bucket.contains(servent)) {
            return -1;
        } else if (bucket.size() < AppConfig.BUCKET_SIZE) {
            bucket.add(servent);
            return 0;
        }
        AppConfig.timestampedErrorPrint("Full Bucket: " + servent.getHashId());
        return -2;
    }

    @Override
    public void delete(ServentInfo servent) {
        Bucket bucket = this.findBucket(servent.getHashId());
        bucket.remove(servent);
    }

    /**
     * Nalazi K najblizih node-ova pakuje ih u FindNodeAnswer strukturu koja sadrzi listu node-ova i trebalo bi to da vratimo (posaljemo tom destination nodu)
     * @param destinationId ->
     * @return
     */
    @Override
    public FindNodeAnswer findClosest(int destinationId) {
        Map<ServentInfo, Integer> kClosestNodes = new LinkedHashMap<>(); // node and distance from destination so we can sort it ascending by value and get first K
        Bucket bucket = this.findBucket(destinationId); // gde se nalazi node koji trazimo, u kom bucket-u

        addToMap(bucket, kClosestNodes, destinationId);

        for(int i = 1; kClosestNodes.size() < AppConfig.BUCKET_SIZE &&
                ((bucket.getId() - i) >= 0 || (bucket.getId() + i) <= AppConfig.ID_SIZE); i++) {

            if (bucket.getId() - i >= 0) {
                Bucket bucketP = this.buckets.get(bucket.getId() - i);
                addToMap(bucketP, kClosestNodes, destinationId);
            }

            if (bucket.getId() + i <= AppConfig.ID_SIZE) {
                Bucket bucketN = this.buckets.get(bucket.getId() + i);
                addToMap(bucketN, kClosestNodes, destinationId);
            }
        }

        List<ServentInfo> sorted = kClosestNodes.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .toList();

        sorted.forEach(System.out::println);

        return new FindNodeAnswer(destinationId, sorted);
    }

    @Override
    public boolean contains(int id) {
        Bucket bucket = this.findBucket(id);
        return bucket.contains(id);
    }

    @Override
    public List<Bucket> getBuckets() {
        return this.buckets;
    }

    @Override
    public int getDistance(int id) {
        return id ^ AppConfig.myServentInfo.getHashId();
    }

    @Override
    public Map<Integer, String> getValueMap() {
        return this.valueMap;
    }

    @Override
    public void putValue(int key, String value) {
        this.valueMap.putIfAbsent(key, value);
    }

    @Override
    public String getValue(int key) {
        return this.valueMap.get(key);
    }

    @Override
    public void removeValue(int key) {
        this.valueMap.remove(key);
    }

    @Override
    public boolean containsValue(int key) { //contains value for Key, lose ime funkcije al ne moze drugacije.
        return this.valueMap.containsKey(key);
    }

    private void addToMap(Bucket bucket, Map<ServentInfo, Integer> answerNodes, int destinationId) {
        for(int id : bucket.getNodeIds()) {
            ServentInfo node = bucket.getNode(id);
            answerNodes.put(node, node.getHashId()^destinationId);
        }
    }
}
