package servent.handler;

import app.AppConfig;
import app.mutex.Token;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.ReplyTokenMessage;

public class ReplyTokenHandler implements MessageHandler{
    private Message clientMessage;

    public ReplyTokenHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.REPLY_TOKEN) {
            AppConfig.timestampedStandardPrint("Got token!");
            Token token = ((ReplyTokenMessage)clientMessage).getToken();
            AppConfig.mutex.receiveToken(token);
        } else {
            AppConfig.timestampedErrorPrint("REPLY_TOKEN handler got: " + clientMessage);
        }
    }
}
