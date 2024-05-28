package servent.message.util;

import app.AppConfig;
import app.Cancellable;
import app.ServentInfo;
import app.Sleepable;
import app.kademlia.FindNodeAnswer;
import servent.message.DHTPutMessage;

public class RepublishValue implements Runnable, Cancellable, Sleepable {

    private final int key;
    private final String value;

    // mozda mi nece ovo trebati.
    private volatile boolean working = true;

    public RepublishValue(String value) {
        this.key = AppConfig.valueHash(value);
        this.value = value;
    }

    @Override
    public void run() {
        while(this.working) {
            try {
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            FindNodeAnswer findNodeAnswer = AppConfig.routingTable.findClosest(key);
            for (ServentInfo serventInfo : findNodeAnswer.getNodes()) {
                if (AppConfig.myServentInfo.getHashId() == serventInfo.getHashId()) {
                    continue;
                }

                DHTPutMessage dhtPutMessage = new DHTPutMessage(AppConfig.myServentInfo, serventInfo, AppConfig.myServentInfo, value);
                dhtPutMessage.addToClosestNodes(serventInfo);
                MessageUtil.sendMessage(dhtPutMessage);
            }
        }
    }

    @Override
    public void stop() {
        this.working = false;
    }

    @Override
    public void sleep(int length) {
        try{
            Thread.sleep(length);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
