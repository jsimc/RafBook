package cli.command;

import app.AppConfig;
import app.ServentInfo;
import servent.message.PingMessage;
import servent.message.util.MessageUtil;

public class PingCommand implements CLICommand{
    @Override
    public String commandName() {
        return "ping";
    }

    @Override
    public void execute(String args) { // ping 1 --> ping hash(1200) == ping 48
        int nodeToPingHashId = -1;
        try {
            nodeToPingHashId = Integer.parseInt(args);

            if(nodeToPingHashId < 0) {
                throw new NumberFormatException();
            }
            ServentInfo nodeToPing = null;
            if(AppConfig.routingTable.contains(nodeToPingHashId)) {
                nodeToPing = AppConfig.routingTable.findBucket(nodeToPingHashId).getNode(nodeToPingHashId);
            }
            if(nodeToPing != null) {
                MessageUtil.sendMessage(new PingMessage(AppConfig.myServentInfo, nodeToPing));
            }
        } catch (NumberFormatException e) {
            AppConfig.timestampedErrorPrint("Pause command should have one int argument, which is time in ms.");
        }

    }
}
