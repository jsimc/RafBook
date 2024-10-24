package cli.command;

import app.AppConfig;
import app.ServentInfo;
import app.kademlia.FindNodeAnswer;
import servent.message.DHTGetMessage;
import servent.message.util.MessageUtil;

import java.util.concurrent.atomic.AtomicInteger;

public class DHTGetCommand implements CLICommand{
    @Override
    public String commandName() {
        return "get";
    }

    // trazi se kao node lookup samo sto ako se ispostavi da neki node ima info zaustavlja se lookup. ne trazi se dalje.
    @Override
    public void execute(String args) { // get <FILE_NAME>
        if(args.split(" ").length == 1) {
            int key = AppConfig.valueHash(args); // args == filename
            if(AppConfig.routingTable.containsValue(key)) {
                AppConfig.timestampedStandardPrint("Value: " + AppConfig.routingTable.getValue(key));
            } else {
                synchronized (AppConfig.lock) {
                    AppConfig.mutex.lock();
//                try {
//                    Thread.sleep(10_000);
//                } catch (InterruptedException e) {
//                    throw new RuntimeException(e);
//                }
                    FindNodeAnswer findNodeAnswer = AppConfig.routingTable.findClosest(key);
                    findNodeAnswer.getNodes().forEach(serventInfo -> {
                        if(serventInfo.getHashId() == AppConfig.myServentInfo.getHashId()) return;
                        DHTGetMessage dhtGetMessage = new DHTGetMessage(AppConfig.myServentInfo, serventInfo, key, AppConfig.myServentInfo, 0);
                        MessageUtil.sendMessage(dhtGetMessage);
                    });
                }
            }
        } else {
            AppConfig.timestampedErrorPrint("Invalid argument for dht_get: " + args + ". Should be filename, which is one string.");
        }
    }
}
