package app.threads;

import app.*;
import app.kademlia.FindNodeAnswer;
import servent.message.DHTPutMessage;
import servent.message.util.MessageUtil;

public class RepublishValue implements Runnable, Cancellable, Sleepable {

    private final int key;
    private final MyFile value;

    // mozda mi nece ovo trebati.
    private volatile boolean working = true;

    public RepublishValue(MyFile value) {
        this.key = value.getKey();
        this.value = value;
    }

    @Override
    public void run() {
        while(this.working) {
            try {
                Thread.sleep(AppConfig.REPUBLISH_TIME_MS);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            FindNodeAnswer findNodeAnswer = AppConfig.routingTable.findClosest(key);
            for (ServentInfo serventInfo : findNodeAnswer.getNodes()) {
                if (AppConfig.myServentInfo.getHashId() == serventInfo.getHashId()) {
                    continue;
                }

                DHTPutMessage dhtPutMessage = new DHTPutMessage(AppConfig.myServentInfo, serventInfo, value);
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

    public int getKey() {
        return key;
    }

    public MyFile getValue() {
        return value;
    }
}
