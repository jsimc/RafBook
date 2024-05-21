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
    public void execute(String args) {
        try {
            int key = Integer.parseInt(args);
            if(AppConfig.routingTable.containsValue(key)) {
                AppConfig.timestampedStandardPrint("Value: " + AppConfig.routingTable.getValue(key));
            } else {
                FindNodeAnswer findNodeAnswer = AppConfig.routingTable.findClosest(key);
                for (ServentInfo serventInfo : findNodeAnswer.getNodes()) {
                    if(serventInfo.getHashId() == AppConfig.myServentInfo.getHashId()) continue;
                    DHTGetMessage dhtGetMessage = new DHTGetMessage(AppConfig.myServentInfo.getListenerPort(), serventInfo.getListenerPort(), key, AppConfig.myServentInfo.getListenerPort(), new AtomicInteger(0));
                    MessageUtil.sendMessage(dhtGetMessage);
                }
            }
        } catch (NumberFormatException e) {
            AppConfig.timestampedErrorPrint("Invalid argument for dht_get: " + args + ". Should be key, which is an int.");
        }
    }
}
