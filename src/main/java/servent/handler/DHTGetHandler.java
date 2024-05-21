package servent.handler;

import app.AppConfig;
import app.ServentInfo;
import app.kademlia.FindNodeAnswer;
import servent.message.*;
import servent.message.util.MessageUtil;

import java.util.concurrent.atomic.AtomicInteger;

public class DHTGetHandler implements MessageHandler {
    private Message clientMessage;

    public DHTGetHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.ASK_GET) {
            // treba da potrazim kod sebe da li ja imam, ako nemam saljem kClosest svojima da potraze sa sve informacijom o OriginalSender.
            // ako nadjem onda saljem original Sender.
            DHTGetMessage dhtGetMessage = (DHTGetMessage) clientMessage;
            int key = dhtGetMessage.getKey();
            int originalSenderPort = dhtGetMessage.getOriginalSenderPort();
            if(AppConfig.routingTable.containsValue(key)) {
                // saljemo dht tell poruku originalnom senderu, ako ne
                DHTTellGetMessage dhtTellGetMessage = new DHTTellGetMessage(AppConfig.myServentInfo.getListenerPort(), originalSenderPort, key, AppConfig.routingTable.getValue(key));
                MessageUtil.sendMessage(dhtTellGetMessage);
            } else {
                int counter = dhtGetMessage.getCounter().incrementAndGet();
                if(counter > Math.pow(2, AppConfig.ID_SIZE)) {
                    // ako smo pregledali sve i nismo uspeli da nadjemo onda vrati fail message
                    DHTGetFailMessage dhtGetFailMessage = new DHTGetFailMessage(AppConfig.myServentInfo.getListenerPort(), originalSenderPort, key);
                    MessageUtil.sendMessage(dhtGetFailMessage);
                    return;
                }
                FindNodeAnswer findNodeAnswer = AppConfig.routingTable.findClosest(key);
                for (ServentInfo serventInfo : findNodeAnswer.getNodes()) {
                    DHTGetMessage dhtGetMessage1 = new DHTGetMessage(AppConfig.myServentInfo.getListenerPort(),
                            serventInfo.getListenerPort(), key, originalSenderPort, dhtGetMessage.getCounter());
                    MessageUtil.sendMessage(dhtGetMessage1);
                }
            }
        } else {
            AppConfig.timestampedErrorPrint("ASK_GET Handler got something else: " + clientMessage.getMessageType());
        }
    }
}
