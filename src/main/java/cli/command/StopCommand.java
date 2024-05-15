package cli.command;

import app.AppConfig;
import cli.CLIParser;
import servent.SimpleServentListener;

public class StopCommand implements CLICommand{

    private CLIParser cliParser;
    private SimpleServentListener simpleServentListener;
    public StopCommand(CLIParser parser, SimpleServentListener listener) {
        this.cliParser = parser;
        this.simpleServentListener = listener;
    }

    @Override
    public String commandName() {
        return "stop";
    }

    @Override
    public void execute(String args) {
        AppConfig.timestampedStandardPrint("Stopping...");
        cliParser.stop();
        simpleServentListener.stop();
    }
}
