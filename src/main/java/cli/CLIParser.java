package cli;

import app.Cancellable;
import cli.command.*;
import servent.SimpleServentListener;

import java.util.ArrayList;
import java.util.List;

public class CLIParser implements Runnable, Cancellable {
    private volatile boolean working = true;

    private final List<CLICommand> commandList;

    public CLIParser(SimpleServentListener simpleServentListener) {
        this.commandList = new ArrayList<>();

        commandList.add(new InfoCommand());
        commandList.add(new PauseCommand());
//        commandList.add(new SuccessorInfo());
        commandList.add(new DHTGetCommand());
        commandList.add(new DHTPutCommand());
        commandList.add(new StopCommand(this, simpleServentListener));
    }

    @Override
    public void run() {

    }

    @Override
    public void stop() {
        this.working = false;
    }
}
