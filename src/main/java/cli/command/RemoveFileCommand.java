package cli.command;

import app.AppConfig;
import app.ServentInfo;
import app.kademlia.FindNodeAnswer;
import servent.message.RemoveFileMessage;
import servent.message.util.MessageUtil;

import java.util.HashSet;

public class RemoveFileCommand implements CLICommand{
    @Override
    public String commandName() {
        return "remove_file";
    }

    @Override
    public void execute(String args) { // remove_file <filepath>
        String[] splitArgs = args.split(" ");
        if(splitArgs.length == 1) {
            String filepath = splitArgs[0];
            int key = AppConfig.valueHash(filepath);

            if(AppConfig.routingTable.removeValue(key) != null) AppConfig.timestampedStandardPrint("Removing file: " + filepath);
            FindNodeAnswer findNodeAnswer = AppConfig.routingTable.findClosest(key);
            for(ServentInfo si : findNodeAnswer.getNodes()) {
                RemoveFileMessage removeFileMessage = new RemoveFileMessage(AppConfig.myServentInfo, si, key, new HashSet<>(findNodeAnswer.getNodes()));
                MessageUtil.sendMessage(removeFileMessage);
            }
        } else {
            AppConfig.timestampedErrorPrint("Invalid arguments for remove_file.");
        }
    }
}
