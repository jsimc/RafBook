package cli.command;

import app.AppConfig;
import app.threads.RepublishValue;
import cli.CLIParser;
import servent.SimpleServentListener;
import app.threads.PingRunnable;

import java.util.List;

public class SleepCommand implements CLICommand{
    private CLIParser cliParser;
    private SimpleServentListener simpleServentListener;
    private PingRunnable pingRunnable;
    private List<RepublishValue> threads;
    public SleepCommand(CLIParser parser, SimpleServentListener listener, PingRunnable pingRunnable, List<RepublishValue> threads) {
        this.cliParser = parser;
        this.simpleServentListener = listener;
        this.pingRunnable = pingRunnable;
        this.threads = threads;
    }

    @Override
    public String commandName() {
        return "sleep";
    }

    @Override
    public void execute(String args) {
        int length = -1;
        if(args.split(" ").length != 1) {
            AppConfig.timestampedErrorPrint("Length of sleeping needed!");
            return;
        }
        try {
            length = Integer.parseInt(args);
        } catch (NumberFormatException e) {
            AppConfig.timestampedErrorPrint("Length of sleeping needed!");
            return;
        }

        AppConfig.timestampedStandardPrint("Sleeping...");
        cliParser.sleep(length);
        simpleServentListener.sleep(length);
        pingRunnable.sleep(length);
        for (RepublishValue rv : threads) {
            rv.sleep(length);
        }
    }
}
