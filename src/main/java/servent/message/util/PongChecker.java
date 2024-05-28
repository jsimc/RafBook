package servent.message.util;

import app.AppConfig;
import app.ServentInfo;

public class PongChecker implements Runnable{

    private ServentInfo serventInfo;

    public PongChecker(ServentInfo serventInfo) {
        this.serventInfo = serventInfo;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if(!AppConfig.isAlive.get(serventInfo)) {
            // treba se zabrinuti
            AppConfig.timestampedErrorPrint("Servent: " + serventInfo + " may not be alive. Need to check it.");
        }
    }
}
