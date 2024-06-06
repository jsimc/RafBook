package cli.command;

import app.AppConfig;
import cli.CLIParser;
import servent.SimpleServentListener;
import servent.message.util.PingRunnable;

import java.util.concurrent.ExecutorService;

public class StopCommand implements CLICommand{

    private CLIParser cliParser;
    private SimpleServentListener simpleServentListener;
    private PingRunnable pingRunnable;
    private ExecutorService republishThreadPool;
    public StopCommand(CLIParser parser, SimpleServentListener listener, PingRunnable pingRunnable, ExecutorService republishThreadPool) {
        this.cliParser = parser;
        this.simpleServentListener = listener;
        this.pingRunnable = pingRunnable;
        this.republishThreadPool = republishThreadPool;
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
        republishThreadPool.shutdown();
    }
}
