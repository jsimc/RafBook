package servent.message;

import app.ServentInfo;

public class RequestTokenMessage extends BasicMessage {
    private static final long serialVersionUID = 2084490973699262440L;

    private final ServentInfo originalSender;
    private final int updatedSeqNumber;

    public RequestTokenMessage(ServentInfo sender, ServentInfo receiver, ServentInfo originalSender, int updatedSeqNumber) {
        super(MessageType.REQ_TOKEN, sender, receiver);
        this.originalSender = originalSender;
//        updatedSeqNumber is sn
        this.updatedSeqNumber = updatedSeqNumber;
    }

    public int getUpdatedSeqNumber() {
        return updatedSeqNumber;
    }

    public ServentInfo getOriginalSender() {
        return originalSender;
    }
}
