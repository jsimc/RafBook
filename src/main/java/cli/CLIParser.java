package cli;

import app.AppConfig;
import app.Cancellable;
import app.Sleepable;
import cli.command.*;
import servent.SimpleServentListener;
import servent.message.util.PingRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CLIParser implements Runnable, Cancellable, Sleepable {
    private volatile boolean working = true;

    private final List<CLICommand> commandList;

    private final ExecutorService republishThreadPool = Executors.newCachedThreadPool();

    public CLIParser(SimpleServentListener simpleServentListener, PingRunnable pingRunnable) {
        this.commandList = new ArrayList<>();

        commandList.add(new InfoCommand());
        commandList.add(new PauseCommand());
        commandList.add(new PingCommand());
        commandList.add(new DHTGetCommand());
        commandList.add(new DHTPutCommand(republishThreadPool));
        commandList.add(new AddFriendCommand());
        commandList.add(new ViewFilesCommand());
        commandList.add(new RemoveFileCommand());
        commandList.add(new SleepCommand(this, simpleServentListener, pingRunnable));
        commandList.add(new StopCommand(this, simpleServentListener, pingRunnable, republishThreadPool));
    }

    @Override
    public void run() {
        Scanner sc = new Scanner(System.in);

        while(working) {
            String commandLine = sc.nextLine();

            int spacePos = commandLine.indexOf(" ");

            String commandName = null;
            String commandArgs = null;
            if(spacePos != -1) {
                commandName = commandLine.substring(0, spacePos);
                commandArgs = commandLine.substring(spacePos+1, commandLine.length());
            } else {
                commandName = commandLine;
            }

            boolean found = false;

            for (CLICommand cliCommand : commandList) {
                if(cliCommand.commandName().equals(commandName)) {
                    cliCommand.execute(commandArgs);
                    found = true;
                    break;
                }
            }

            if (!found) {
                AppConfig.timestampedErrorPrint("Unknown command: " + commandName);
            }
        }
        sc.close();
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
