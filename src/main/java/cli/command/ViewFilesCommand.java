package cli.command;

import app.AppConfig;
import app.kademlia.FindNodeAnswer;
import servent.message.ViewFilesMessage;
import servent.message.util.MessageUtil;

import java.util.HashSet;

public class ViewFilesCommand implements CLICommand{
    @Override
    public String commandName() {
        return "view_files";
    }

    @Override
    public void execute(String args) { // view_files 1225
        if(args.split(" ").length == 1) {
            try {
                int port = Integer.parseInt(args.split(" ")[0]);
                int portHashId = AppConfig.hash(port);

                FindNodeAnswer findNodeAnswer = AppConfig.routingTable.findClosest(portHashId);
                findNodeAnswer.getNodes().forEach(serventInfo -> {
                    ViewFilesMessage viewFilesMessage = new ViewFilesMessage(AppConfig.myServentInfo, serventInfo, AppConfig.myServentInfo, portHashId, new HashSet<>(findNodeAnswer.getNodes()));
                    MessageUtil.sendMessage(viewFilesMessage);
                });
            } catch (NumberFormatException e) {
                AppConfig.timestampedStandardPrint("Argument for view_files should be integer.");
            }
        } else {
            AppConfig.timestampedErrorPrint("Invalid arguments for view_files.");
        }
    }
}
