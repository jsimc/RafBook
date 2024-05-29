package servent.message.util;

import app.AppConfig;
import app.ServentInfo;
import app.kademlia.FindNodeAnswer;
import servent.message.CheckNodeMessage;

public class PongChecker implements Runnable{

    private ServentInfo serventInfo;

    public PongChecker(ServentInfo serventInfo) {
        this.serventInfo = serventInfo;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(AppConfig.SOFT_RESET_MS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(!AppConfig.isAlive.get(serventInfo)) {
            // treba se zabrinuti --> soft reset
            AppConfig.timestampedErrorPrint("Servent: " + serventInfo + " may not be alive. Need to check it.");
            FindNodeAnswer findNodeAnswer = AppConfig.routingTable.findClosest(serventInfo.getHashId());
            for(ServentInfo si : findNodeAnswer.getNodes()) {
                CheckNodeMessage checkNodeMessage = new CheckNodeMessage(AppConfig.myServentInfo, si, this.serventInfo);
                MessageUtil.sendMessage(checkNodeMessage);
            }
        }
    }
}
