package app.mutex;

import app.AppConfig;
import app.ServentInfo;
import app.kademlia.ServentInfoHashIdComparator;

import java.io.Serializable;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

public class Token implements Serializable {
    private static final long serialVersionUID = 2084690973699262440L;

    // LN, key: Servent, value: Seq number for the last time servent used token.
    private Map<ServentInfo, Integer> ln;

    // Q
    private Queue<ServentInfo> queue;

    public Token() {
        ln = new ConcurrentHashMap<>();
        queue = new PriorityBlockingQueue<>(AppConfig.SERVENT_COUNT, new ServentInfoHashIdComparator());
    }

    public Map<ServentInfo, Integer> getLn() {
        return ln;
    }

    public Queue<ServentInfo> getQueue() {
        return queue;
    }

    public int getLNForServentInfo(ServentInfo serventInfo) {
        return this.ln.getOrDefault(serventInfo, 0);
    }

    public int updateLN(ServentInfo serventInfo, int rn) {
        return this.ln.compute(serventInfo, (serventInfo1, integer) -> rn);
    }
    public void putInQueue(ServentInfo serventInfo) {
        this.queue.add(serventInfo);
    }

    public ServentInfo popFromQueue() {
        return this.queue.remove();
    }

    public boolean isQueueEmpty() {
        return this.queue.isEmpty();
    }

    @Override
    public String toString() {
        return "Token{" +
                "ln=" + ln +
                ", queue=" + queue +
                '}';
    }
}
