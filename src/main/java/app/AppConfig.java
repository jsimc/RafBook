package app;

import app.kademlia.RoutingTable;
import app.kademlia.RoutingTableImpl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

public class AppConfig {

    // or just: "workspace" ???
    public static final String WORKSPACE = "C:\\Users\\Jelena\\Documents\\faks\\8. semestar\\KiDs\\projekat_kids\\kids_pr_jelena_simic_rn2720\\workspace";

    public static ServentInfo myServentInfo;

    public static boolean INITIALIZED = false;

    public static int BOOTSTRAP_PORT;

    public static int SERVENT_COUNT;

    ///////////////////////////////////////////////////
    public static int ID_SIZE; // number of nodes in the system = Math.pow(2, ID_SIZE);
    public static int BUCKET_SIZE; // size of each bucket (ovo izgleda JESTE broj K!!!)
    public static int PING_SCHEDULE_TIME_VALUE; // in milliseconds

    public static Map<ServentInfo, Boolean> isAlive = new ConcurrentHashMap<>(); // key: servent, value: true if alive.

    public static RoutingTable routingTable;

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

        try {
            ID_SIZE = Integer.parseInt(properties.getProperty("id_size"));
        } catch (NumberFormatException e) {
            timestampedErrorPrint("Problem reading id_size. Exiting...");
            System.exit(0);
        }

        try {
            BUCKET_SIZE = Integer.parseInt(properties.getProperty("bucket_size"));
        } catch (NumberFormatException e) {
            timestampedErrorPrint("Problem reading bucket_size. Exiting...");
            System.exit(0);
        }

        try {
            PING_SCHEDULE_TIME_VALUE = Integer.parseInt(properties.getProperty("ping_schedule_time"));
        } catch (NumberFormatException e) {
            timestampedErrorPrint("Problem reading ping_schedule_time. Exiting...");
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


        myServentInfo = new ServentInfo(serventId,"localhost", serventPort);
        routingTable = new RoutingTableImpl();
        routingTable.update(myServentInfo);
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

    public static int hash(int value) {
        return Math.abs(61 * value) % (int) Math.pow(2, ID_SIZE);
    }

    public static int valueHash(String value) {
        return hash(value.hashCode());
    }
}
