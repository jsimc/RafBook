package app.kademlia;

import app.ServentInfo;

import java.util.Comparator;

public class DistanceComparator implements Comparator<ServentInfo> {

    private int key;

    public DistanceComparator(int key) {
        this.key = key;
    }

    @Override
    public int compare(ServentInfo o1, ServentInfo o2) {
        int diff1 = o1.getHashId() ^ key;
        int diff2 = o2.getHashId() ^ key;
        return Integer.compare(diff1, diff2);
    }
}
