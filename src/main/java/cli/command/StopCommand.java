package cli.command;

import app.AppConfig;
import cli.CLIParser;
import servent.SimpleServentListener;
import servent.message.util.PingRunnable;

public class StopCommand implements CLICommand{

    private CLIParser cliParser;
    private SimpleServentListener simpleServentListener;
    private PingRunnable pingRunnable;
    public StopCommand(CLIParser parser, SimpleServentListener listener, PingRunnable pingRunnable) {
        this.cliParser = parser;
        this.simpleServentListener = listener;
        this.pingRunnable = pingRunnable;
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
        pingRunnable.stop();
    }
}
