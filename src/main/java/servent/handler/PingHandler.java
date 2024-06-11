package servent.handler;

import app.AppConfig;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.PongMessage;
import servent.message.util.MessageUtil;

import java.util.concurrent.atomic.AtomicBoolean;

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
            // updateRoutingTable!

            if(AppConfig.sleep) {
                try {
                    Thread.sleep(AppConfig.SOFT_RESET_MS+1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                AppConfig.sleep = false;
            }


            AppConfig.routingTable.update(clientMessage.getSender());
            // stavicu kod sebe tog sendera da vidim da je ziv
            AppConfig.isAlive.put(clientMessage.getSender(), true);

            // saljem pong poruku odgovora
            MessageUtil.sendMessage(
                    new PongMessage(clientMessage.getReceiver(), clientMessage.getSender()));

        } else {
            AppConfig.timestampedErrorPrint("PING handler got: " + clientMessage);
        }
    }
}
