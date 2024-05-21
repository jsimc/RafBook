package cli.command;

import app.AppConfig;

public class DHTGetCommand implements CLICommand{
    @Override
    public String commandName() {
        return "get";
    }

    // trazi se kao node lookup samo sto ako se ispostavi da neki node ima info zaustavlja se lookup. ne trazi se dalje.
    @Override
    public void execute(String args) {
        try {
            int key = Integer.parseInt(args);
        } catch (NumberFormatException e) {
            AppConfig.timestampedErrorPrint("Invalid argument for dht_get: " + args + ". Should be key, which is an int.");
        }
    }
}
