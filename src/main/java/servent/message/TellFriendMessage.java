package servent.message;

import app.ServentInfo;

public class TellFriendMessage extends BasicMessage{
    private static final long serialVersionUID = -71866183555507085L;

    private final ServentInfo friendInfo;
    public TellFriendMessage(ServentInfo sender, ServentInfo receiver, ServentInfo friendInfo) {
        super(MessageType.TELL_ADD_FRIEND, sender, receiver);
        this.friendInfo = friendInfo;
    }

    public ServentInfo getFriendInfo() {
        return friendInfo;
    }
}
