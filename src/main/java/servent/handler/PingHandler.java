package servent.handler;

import app.AppConfig;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.PongMessage;
import servent.message.util.MessageUtil;

public class PingHandler implements MessageHandler{
    private final Message clientMessage;

    public PingHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        //Yap ... it's a PING
        if (clientMessage.getMessageType() == MessageType.PING) {
            /*
             * When we get a PING, we send a PONG back.
             * Notice that this is NOT on the same socket, though,
             * because we want this system to be completely asynchronous.
             */
            MessageUtil.sendMessage(
                    new PongMessage(clientMessage.getReceiverPort(), clientMessage.getSenderPort()));

        } else {
            AppConfig.timestampedErrorPrint("PING handler got: " + clientMessage);
        }
    }
}
