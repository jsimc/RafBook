package cli.command;

import app.AppConfig;
import cli.CLIParser;
import servent.SimpleServentListener;
import servent.message.util.PingRunnable;

public class SleepCommand implements CLICommand{
    private CLIParser cliParser;
    private SimpleServentListener simpleServentListener;
    private PingRunnable pingRunnable;
    public SleepCommand(CLIParser parser, SimpleServentListener listener, PingRunnable pingRunnable) {
        this.cliParser = parser;
        this.simpleServentListener = listener;
        this.pingRunnable = pingRunnable;
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
    }
}
