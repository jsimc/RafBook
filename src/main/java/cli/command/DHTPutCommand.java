package cli.command;

import app.AppConfig;
import app.ServentInfo;
import app.kademlia.FindNodeAnswer;
import servent.message.DHTPutMessage;
import servent.message.MessageType;
import servent.message.util.MessageUtil;

import java.io.File;

public class DHTPutCommand implements CLICommand {
    @Override
    public String commandName() {
        return "put";
    }


    // "put" trazi se k closest to the key (just like the node) and then every one of those k closest gets a value.
    @Override
    public void execute(String args) { // dht 25 aaaaa
        String[] splitArgs = args.split(" ");
        if(splitArgs.length == 2) {
            int key = 0;
            String value = "";
            try {
                key = Integer.parseInt(splitArgs[0]);
                value = splitArgs[1];

                if (key < 0 || key >= Math.pow(2, AppConfig.ID_SIZE)) {
                    throw new NumberFormatException();
            }
                File file = new File(value);
                // TODO
                //  if file is not in directory workspace (doesn't need to be directly in it)
                //  then throw exception !
                // dobila sam put komandu sa kez and value
                // treba da trazim kClosest
                if(AppConfig.routingTable.containsValue(key)) {
                    // ne bi trebalo nista da uradim.
                    AppConfig.timestampedErrorPrint("Already have key: " + key);
                    return;
                }
                FindNodeAnswer findNodeAnswer = AppConfig.routingTable.findClosest(key);

                for(ServentInfo serventInfo : findNodeAnswer.getNodes()) {
                    // proveravam ako sam ja u toj kClosest ubacujem sebi:
                    if(AppConfig.myServentInfo.getHashId() == serventInfo.getHashId()) {
                        AppConfig.routingTable.putValue(key, value);
                        continue;
                    }

                    DHTPutMessage dhtPutMessage = new DHTPutMessage(AppConfig.myServentInfo, serventInfo, key, value);
                    MessageUtil.sendMessage(dhtPutMessage);
                }

            } catch (NumberFormatException e) {
                AppConfig.timestampedErrorPrint("Invalid key and value pair. Key should be integer between 0 and " + Math.pow(2, AppConfig.ID_SIZE)
                        + ". Value should be string indicating to file in directory: " + AppConfig.WORKSPACE);
            }
        } else {
            AppConfig.timestampedErrorPrint("Invalid arguments for put");
        }
    }
}
