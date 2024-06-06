package cli.command;

import app.AppConfig;
import app.MyFile;
import app.ServentInfo;
import app.kademlia.Bucket;

import java.util.Map;

public class InfoCommand implements CLICommand{
    @Override
    public String commandName() {
        return "info";
    }

    @Override
    public void execute(String args) {
        AppConfig.timestampedStandardPrint("My info: " + AppConfig.myServentInfo);
        AppConfig.timestampedStandardPrint("My routing table: ");
        for (Bucket bucket : AppConfig.routingTable.getBuckets()) {
            AppConfig.timestampedStandardPrint("Bucket " + bucket.getId());
            for(Integer nodeId : bucket.getNodeIds()) {
                AppConfig.timestampedStandardPrint("Node: " + bucket.getNode(nodeId));
            }
        }

        for(Map.Entry<Integer, MyFile> entry : AppConfig.routingTable.getValueMap().entrySet()) {
            AppConfig.timestampedStandardPrint("key: " + entry.getKey() + ", value: " + entry.getValue());
        }

        AppConfig.timestampedStandardPrint("friends: ");
        for(ServentInfo si : AppConfig.myServentInfo.getFriends()) {
            AppConfig.timestampedStandardPrint(String.valueOf(si));
        }
    }
}
