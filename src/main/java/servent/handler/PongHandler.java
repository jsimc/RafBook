package servent.handler;

import app.AppConfig;
import servent.message.Message;
import servent.message.MessageType;

public class PongHandler implements MessageHandler {
    private Message clientMessage;

    public PongHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        /*
         * The receive function is gonna print out that we got this message anyway,
         * so idk what we are doing here tbh.
         * All of this code is quite useless, and we even made a thread for it,
         * I mean it's rather divorced from reality if you ask me.
         * Obviously we needed this long comment here so we can feel important.
         * k tnx bye.
         */
        if (clientMessage.getMessageType() == MessageType.PONG) {
            // kad dobijem pong znaci da je taj node odgovorio na moj PING
            AppConfig.routingTable.update(clientMessage.getSender());
            // znaci da je ziv i da mu postavljam isAlive na true
            AppConfig.isAlive.put(clientMessage.getSender(), true);
        }  else {
            AppConfig.timestampedErrorPrint("PONG handler got: " + clientMessage);
        }
    }
}
