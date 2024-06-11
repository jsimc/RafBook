package cli.command;

import app.AppConfig;
import app.MyFile;
import app.ServentInfo;
import app.kademlia.FindNodeAnswer;
import app.threads.RepublishValue;
import servent.message.DHTPutMessage;
import servent.message.util.MessageUtil;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class DHTPutCommand implements CLICommand {

    private final List<RepublishValue> threads;
    public DHTPutCommand(List<RepublishValue> threads) {
        this.threads = threads;
    }

    @Override
    public String commandName() {
        return "put";
    }

    // "put" trazi se k closest to the key (just like the node) and then every one of those k closest gets a value.
    @Override
    public void execute(String args) { // put <file_path> <public/private>
        String[] splitArgs = args.split(" ");
        if(splitArgs.length == 2) {
            int key;
            String value = "";
            Boolean isPublic;
            synchronized (AppConfig.lock) {
                try {
                    value = splitArgs[0];
                    key = AppConfig.valueHash(value);
                    isPublic = splitArgs[1].equals("public") ? Boolean.TRUE : (splitArgs[1].equals("private") ? Boolean.FALSE : null);

                    if(isPublic == null) {
                        AppConfig.timestampedStandardPrint("File can be either \"public\" or \"private\". You entered: " + splitArgs[1]);
                        return;
                    }

                    File file = new File(String.valueOf(Path.of(AppConfig.WORKSPACE, value)));
                    if(!file.exists() || !file.isFile()) {
                        AppConfig.timestampedErrorPrint("File: " + file.getName() + " does not exist in directory: " + AppConfig.WORKSPACE);
                        return;
                    }

                    MyFile myFile = new MyFile(key, file, AppConfig.myServentInfo, isPublic);

                    if(AppConfig.routingTable.containsValue(key)) {
                        // ne bi trebalo nista da uradim.
                        AppConfig.timestampedStandardPrint("Already have key: " + key);
                        return;
                    }

                    AppConfig.routingTable.putValue(key, myFile);
                    AppConfig.routingTable.addToMyFiles(myFile);
                    FindNodeAnswer findNodeAnswer = AppConfig.routingTable.findClosest(key);

                    for(ServentInfo serventInfo : findNodeAnswer.getNodes()) {
                        if(serventInfo.equals(AppConfig.myServentInfo)) {
    //                        AppConfig.routingTable.putValue(key, value);
                            continue;
                        }

                        DHTPutMessage dhtPutMessage = new DHTPutMessage(AppConfig.myServentInfo, serventInfo, myFile);
                        MessageUtil.sendMessage(dhtPutMessage);
                    }

                    RepublishValue republishValue = new RepublishValue(myFile);
                    Thread thread = new Thread(republishValue);
                    thread.start();
                    this.threads.add(republishValue);
                } catch (NumberFormatException e) {
                    AppConfig.timestampedErrorPrint("Invalid key and value pair. Key should be integer between 0 and " + Math.pow(2, AppConfig.ID_SIZE)
                            + ". Value should be string indicating to file in directory: " + AppConfig.WORKSPACE);
                }
            }
        } else {
            AppConfig.timestampedErrorPrint("Invalid arguments for put");
        }
    }
}
