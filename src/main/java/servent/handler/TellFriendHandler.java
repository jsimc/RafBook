package servent.handler;

import app.AppConfig;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.TellFriendMessage;

public class TellFriendHandler implements MessageHandler{
    private Message clientMessage;
    public TellFriendHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.TELL_ADD_FRIEND) {
            TellFriendMessage tellFriendMessage = (TellFriendMessage) clientMessage;
            if(tellFriendMessage.getFriendInfo() != null) {
                AppConfig.myServentInfo.addFriend(tellFriendMessage.getFriendInfo());
            }
        } else {
            AppConfig.timestampedErrorPrint("TELL_ADD_FRIEND Handler got something else: " + clientMessage.getMessageType());
        }
    }
}
