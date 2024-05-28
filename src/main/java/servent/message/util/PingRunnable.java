package servent.message.util;

import app.AppConfig;
import app.Cancellable;
import app.ServentInfo;
import app.Sleepable;
import servent.message.PingMessage;

/**
 * Thread that will sleep for AppConfig.PING_SCHEDULE_TIME_VALUE and then check if Pong was returned.
 *
 */
public class PingRunnable implements Runnable, Cancellable, Sleepable {
    private volatile boolean working = true;
    @Override
    public void run() {
        while(this.working) {
            try {
                Thread.sleep(10_000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            AppConfig.routingTable.getBuckets().forEach(bucket -> {
                for (Integer nodeId : bucket.getNodeIds()) {
                    ServentInfo node = bucket.getNode(nodeId);
                    MessageUtil.sendMessage(
                            new PingMessage(AppConfig.myServentInfo, node));
                    AppConfig.isAlive.put(node, false);
                    Thread waitForPong = new Thread(new PongChecker(node));
                    waitForPong.start();
                }
            });
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
