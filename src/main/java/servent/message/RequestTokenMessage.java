package servent.message;

import app.ServentInfo;

import java.util.Set;

public class RequestTokenMessage extends BasicMessage {
    private static final long serialVersionUID = 2084490973699262440L;

    private final ServentInfo originalSender;
    private final int updatedSeqNumber;

    private final Set<ServentInfo> serventInfoSet;

    public RequestTokenMessage(ServentInfo sender, ServentInfo receiver, ServentInfo originalSender, int updatedSeqNumber, Set<ServentInfo> serventInfoSet) {
        super(MessageType.REQ_TOKEN, sender, receiver);
        this.originalSender = originalSender;
//        updatedSeqNumber is sn
        this.updatedSeqNumber = updatedSeqNumber;
        this.serventInfoSet = serventInfoSet;
    }

    public int getUpdatedSeqNumber() {
        return updatedSeqNumber;
    }

    public ServentInfo getOriginalSender() {
        return originalSender;
    }

    public Set<ServentInfo> getServentInfoSet() {
        return serventInfoSet;
    }
}
