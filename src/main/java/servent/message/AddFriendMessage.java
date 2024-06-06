package servent.message;

import app.ServentInfo;

import java.util.Set;

public class AddFriendMessage extends BasicMessage{
    private static final long serialVersionUID = -71866183555507085L;

    private final ServentInfo originalSender;
    private final int friendHashId;

    private final Set<ServentInfo> serventInfos;

    public AddFriendMessage(ServentInfo sender, ServentInfo receiver, ServentInfo originalSender, int friendHashId, Set<ServentInfo> serventInfos) {
        super(MessageType.ADD_FRIEND, sender, receiver);
        this.originalSender = originalSender;
        this.friendHashId = friendHashId;
        this.serventInfos = serventInfos;
    }

    public ServentInfo getOriginalSender() {
        return originalSender;
    }

    public int getFriendHashId() {
        return friendHashId;
    }

    public Set<ServentInfo> getServentInfos() {
        return serventInfos;
    }
}
