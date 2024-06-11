package app.kademlia;

import app.AppConfig;
import app.MyFile;
import app.ServentInfo;
import servent.message.PingMessage;
import servent.message.util.MessageUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

public class RoutingTableImpl implements RoutingTable {

    private List<Bucket> buckets;
    private Map<Integer, MyFile> valueMap;
    private List<MyFile> myFiles;

    private List<Bucket> cacheBuckets;

    public RoutingTableImpl() {
        this.buckets = new CopyOnWriteArrayList<>();
        this.valueMap = new ConcurrentHashMap<>();
        this.myFiles = new CopyOnWriteArrayList<>();
        this.cacheBuckets = new CopyOnWriteArrayList<>();
        for (int i = 0; i <= AppConfig.ID_SIZE; i++) { // 0, 1, 2, 3, 4, 5, 6
            buckets.add(new BucketImpl(i));
            cacheBuckets.add(new BucketImpl(i));
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
    public synchronized Bucket findBucket(int id) {
        int xorNumber = this.getDistance(id);
        int prefix = this.getNodePrefix(xorNumber);
        return buckets.get(prefix);
    }

    @Override
    public synchronized Bucket findCacheBucket(int id) {
        int xorNumber = this.getDistance(id);
        int prefix = this.getNodePrefix(xorNumber);
        return cacheBuckets.get(prefix);
    }

    @Override
    public void updateCache(ServentInfo servent) {
        Bucket cacheBucket = this.findCacheBucket(servent.getHashId());
        if(cacheBucket.contains(servent)) {
            cacheBucket.pushToFront(servent);
        } else if (cacheBucket.size() < AppConfig.BUCKET_SIZE) {
            cacheBucket.add(servent);
        }
    }

    /**
     * Returns 0 if servent is added to the bucket, returns -1 if bucket contains servent already,
     * returns -2 if bucket is full and cant add more servents
     * @param servent
     * @return
     */
    @Override
    public synchronized int update(ServentInfo servent){
        Bucket bucket = this.findBucket(servent.getHashId()); // nalazimo bucket u koji cemo da ga stavimo.
        if (bucket.contains(servent)) {
            // pushToFront
            bucket.pushToFront(servent);
            return -1;
        } else if (bucket.size() < AppConfig.BUCKET_SIZE) {
            bucket.add(servent);
            return 0;
        }

        AppConfig.timestampedErrorPrint("Full Bucket: " + servent.getHashId());
        // put it in cache
        this.updateCache(servent);
        return -2;
    }

    @Override
    public void softUpdate(ServentInfo serventInfo) {
        if(this.update(serventInfo) == -2) {
            updateCache(serventInfo);
//            // ako je bucket full onda treba da pingujemo za taj bucket najstarijeg. tj bucket.get(bucket.size() -1)
//            Bucket bucket = findBucket(serventInfo.getHashId());
//            int lreServentId = bucket.getNodeIds().get(bucket.size()-1); // ovo bismo trebali da radimo za svaki a ne samo za lre?
//            ServentInfo lreServent = bucket.getNode(lreServentId);
//            PingMessage pingMessage = new PingMessage(AppConfig.myServentInfo, lreServent);
//            MessageUtil.sendMessage(pingMessage);
        }
    }

    @Override
    public void delete(ServentInfo servent) {
        Bucket bucket = this.findBucket(servent.getHashId());
        bucket.remove(servent);

        // pokusavamo da obrisani cvor zamenimo sa nekim iz cache bucket
        Thread thread = new Thread(() -> {
            Bucket cacheBucket = this.findCacheBucket(servent.getHashId());
            if(!cacheBucket.getNodeIds().isEmpty()) {
                ServentInfo si = cacheBucket.getNode(cacheBucket.getNodeIds().get(0));
                bucket.add(si);
                cacheBucket.remove(si);
            }
        });
        thread.start();
    }

    /**
     * Nalazi K najblizih node-ova pakuje ih u FindNodeAnswer strukturu koja sadrzi listu node-ova i trebalo bi to da vratimo (posaljemo tom destination nodu)
     * @param destinationId ->
     * @return
     */
    @Override
    public synchronized FindNodeAnswer findClosest(int destinationId) {
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

//        sorted.forEach(System.out::println);

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
    public Map<Integer, MyFile> getValueMap() {
        return this.valueMap;
    }

    @Override
    public void putValue(int key, MyFile value) {
        this.valueMap.putIfAbsent(key, value);
    }

    @Override
    public MyFile getValue(int key) {
        return this.valueMap.get(key);
    }

    @Override
    public MyFile removeValue(int key) {
        return this.valueMap.remove(key);
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

    @Override
    public void addToMyFiles(MyFile value) {
        this.myFiles.add(value);
    }

    @Override
    public List<MyFile> getAllMyFiles() {
        return this.myFiles;
    }

    @Override
    public List<MyFile> getPublicMyFiles() {
        return this.myFiles.stream().filter(MyFile::isPublic).collect(Collectors.toList());
    }
}
