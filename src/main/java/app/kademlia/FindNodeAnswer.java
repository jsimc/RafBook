package app.kademlia;

import app.ServentInfo;

import java.io.Serializable;
import java.util.List;

public class FindNodeAnswer implements Serializable {
    private int destinationId;
    private List<ServentInfo> nodes;

    public FindNodeAnswer(int destinationId, List<ServentInfo> nodes) {
        this.nodes = nodes;
        this.destinationId = destinationId;
    }

    public int getDestinationId() {
        return destinationId;
    }

    public List<ServentInfo> getNodes() {
        return nodes;
    }

    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
    }

    public void setNodes(List<ServentInfo> nodes) {
        this.nodes = nodes;
    }

    @Override
    public String toString() {
        return destinationId + ":" +nodes;
    }
}
