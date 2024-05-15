package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class AppConfig {

    public static ServentInfo myServentInfo;

    public static boolean INITIALIZED = false;

    public static int BOOTSTRAP_PORT;

    public static int SERVENT_COUNT;

//    public static ChordState chordState;

    public static void readConfig(String configName, int serventId) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File(configName)));
        } catch (IOException e) {
            timestampedErrorPrint("Couldn't open properties file. Exiting...");
            System.exit(0);
        }

        try {
            BOOTSTRAP_PORT = Integer.parseInt(properties.getProperty("bs.port"));
        } catch (NumberFormatException e) {
            timestampedErrorPrint("Problem reading bootstrap_port. Exiting...");
            System.exit(0);
        }

        try {
            SERVENT_COUNT = Integer.parseInt(properties.getProperty("servent_count"));
        } catch (NumberFormatException e) {
            timestampedErrorPrint("Problem reading servent_count. Exiting...");
            System.exit(0);
        }

        String portProperty = "servent"+serventId+".port";

        int serventPort = -1;

        try {
            serventPort = Integer.parseInt(properties.getProperty(portProperty));
        } catch(NumberFormatException e) {
            timestampedErrorPrint("Problem reading " + portProperty + ". Exiting...");
            System.exit(0);
        }

        myServentInfo = new ServentInfo("localhost", serventPort);
    }

    public static void timestampedStandardPrint(String message) {
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date now = new Date();

        System.out.println(timeFormat.format(now) + " - " + message);
    }

    public static void timestampedErrorPrint(String message) {
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date now = new Date();

        System.err.println(timeFormat.format(now) + " - " + message);
    }
}
