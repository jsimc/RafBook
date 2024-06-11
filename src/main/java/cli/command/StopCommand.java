package cli.command;

import app.AppConfig;
import app.threads.RepublishValue;
import cli.CLIParser;
import servent.SimpleServentListener;
import app.threads.PingRunnable;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class StopCommand implements CLICommand{

    private CLIParser cliParser;
    private SimpleServentListener simpleServentListener;
    private PingRunnable pingRunnable;
    private List<RepublishValue> threads;
    public StopCommand(CLIParser parser, SimpleServentListener listener, PingRunnable pingRunnable, List<RepublishValue> threads) {
        this.cliParser = parser;
        this.simpleServentListener = listener;
        this.pingRunnable = pingRunnable;
        this.threads = threads;
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
        for (RepublishValue rv : threads) {
            rv.stop();
        }
    }
}
