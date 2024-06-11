package app;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ServentInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 5304170042791281555L;
    private final int id;
    private final int hashId;
    private final String ipAddress;
    private final int listenerPort;

    private final Set<ServentInfo> friends;

    public ServentInfo(int id, String ipAddress, int listenerPort) {
        this.ipAddress = ipAddress;
        this.listenerPort = listenerPort;
        this.id = id;
        this.hashId = AppConfig.hash(listenerPort);
        this.friends = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getListenerPort() {
        return listenerPort;
    }

    private int getId() {
        return id;
    }

    public int getHashId() {
        return hashId;
    }

    public Set<ServentInfo> getFriends() {
        return friends;
    }

    public void addFriend(ServentInfo serventInfo) {
        friends.add(serventInfo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServentInfo that = (ServentInfo) o;
        return hashId == that.hashId && listenerPort == that.listenerPort;
    }

    @Override
    public int hashCode() {
        return Objects.hash(hashId, listenerPort);
    }

    @Override
    public String toString() {
        return "[" + hashId + "|" + ipAddress + "|" + listenerPort + "]";
    }
}
