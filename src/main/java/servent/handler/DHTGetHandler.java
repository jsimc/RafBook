package servent.handler;

import app.AppConfig;
import app.ServentInfo;
import app.kademlia.FindNodeAnswer;
import servent.message.*;
import servent.message.util.MessageUtil;

import java.util.concurrent.atomic.AtomicBoolean;

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
            ServentInfo originalSender = dhtGetMessage.getOriginalSender();
            if(AppConfig.routingTable.containsValue(key)) {
                // saljemo dht tell poruku originalnom senderu, ako ne
                DHTTellGetMessage dhtTellGetMessage = new DHTTellGetMessage(AppConfig.myServentInfo, originalSender, key, AppConfig.routingTable.getValue(key));
                MessageUtil.sendMessage(dhtTellGetMessage);
            } else {
//                int counter = dhtGetMessage.getCounter().incrementAndGet();
//                AppConfig.timestampedErrorPrint("counter in get: " + dhtGetMessage.getCounter().get());
//                if(counter > AppConfig.ID_SIZE) { // ovo izgleda ne radi.
//                    // ako smo pregledali sve i nismo uspeli da nadjemo onda vrati fail message
//                    DHTGetFailMessage dhtGetFailMessage = new DHTGetFailMessage(AppConfig.myServentInfo, originalSender, key);
//                    MessageUtil.sendMessage(dhtGetFailMessage);
//                    return;
//                }
                AtomicBoolean sent = new AtomicBoolean(false);
                FindNodeAnswer findNodeAnswer = AppConfig.routingTable.findClosest(key);
                findNodeAnswer.getNodes().forEach(serventInfo -> {
                    if(serventInfo.getHashId() == AppConfig.myServentInfo.getHashId() || serventInfo.getHashId() == dhtGetMessage.getOriginalSender().getHashId())
                        return;
                    DHTGetMessage dhtGetMessage1 = new DHTGetMessage(AppConfig.myServentInfo,
                            serventInfo, key, originalSender, dhtGetMessage.getCounter());
                    MessageUtil.sendMessage(dhtGetMessage1);
                    sent.set(true);
                });
                if(sent.get() == false) {
                    DHTGetFailMessage dhtGetFailMessage = new DHTGetFailMessage(AppConfig.myServentInfo, originalSender, key);
                    MessageUtil.sendMessage(dhtGetFailMessage);
                    return;
                }
            }
        } else {
            AppConfig.timestampedErrorPrint("ASK_GET Handler got something else: " + clientMessage.getMessageType());
        }
    }
}
