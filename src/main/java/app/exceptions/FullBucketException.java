package app.exceptions;

public class FullBucketException extends Exception{
    public FullBucketException(int bucketId, int nodeId) {
        super("Bucket with id: " + bucketId + " is full and can't add node id: " + nodeId);
    }
}
