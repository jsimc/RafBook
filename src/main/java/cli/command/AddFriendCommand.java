package cli.command;

import app.AppConfig;
import app.ServentInfo;
import app.kademlia.FindNodeAnswer;
import servent.message.AddFriendMessage;
import servent.message.util.MessageUtil;

import java.util.HashSet;
import java.util.Set;

public class AddFriendCommand implements CLICommand{
    @Override
    public String commandName() {
        return "add_friend";
    }

    @Override
    public void execute(String args) { //   add_friend <LISTENER_PORT>
        if(args.split(" ").length == 1) {
            try {
                int friendListenerPort = Integer.parseInt(args.split(" ")[0]);
                int friendHashId = AppConfig.hash(friendListenerPort); // hashId

                AppConfig.myServentInfo.addFriend(new ServentInfo(-1, "localhost", friendListenerPort));

                // sada po tome treba da trazimo standardno
                FindNodeAnswer findNodeAnswer = AppConfig.routingTable.findClosest(friendHashId);
                for (ServentInfo si : findNodeAnswer.getNodes()) {
                    AddFriendMessage addFriendMessage = new AddFriendMessage(AppConfig.myServentInfo, si, AppConfig.myServentInfo, friendHashId, new HashSet<>(findNodeAnswer.getNodes()));
                    MessageUtil.sendMessage(addFriendMessage);
                }
            } catch (NumberFormatException e) {
                AppConfig.timestampedErrorPrint("Argument for add_friend should be integer.");
            }
        } else {
            AppConfig.timestampedErrorPrint("Invalid argument for add_friend.");
        }
    }
}
