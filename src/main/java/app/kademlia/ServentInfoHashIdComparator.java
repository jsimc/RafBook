package app.kademlia;

import app.ServentInfo;

import java.io.Serializable;
import java.util.Comparator;

public class ServentInfoHashIdComparator implements Comparator<ServentInfo>, Serializable {

    private static final long serialVersionUID = 208449097367752440L;
    @Override
    public int compare(ServentInfo o1, ServentInfo o2) {
        return Integer.compare(o1.getHashId(), o2.getHashId());
    }
}
