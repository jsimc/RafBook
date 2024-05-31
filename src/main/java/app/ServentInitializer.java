package app;

import servent.message.NewNodeMessage;
import servent.message.util.MessageUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ServentInitializer implements Runnable {

    @Override
    public void run() {
        int someServentPort = getSomeServentPort();

        if(someServentPort == -2) {
            AppConfig.timestampedErrorPrint("Error in contacting bootstrap. Exiting...");
            System.exit(0);
        } else if(someServentPort == -1) { //bootstrap gave us -1 -> we are first
            AppConfig.timestampedStandardPrint("First node in system.");
            AppConfig.mutex.setFirstHaveToken(true);
        } else { //bootstrap gave us something else - let that node tell our successor that we are here
            // new node in the system message
            AppConfig.timestampedStandardPrint("New node in system.");
            ServentInfo someServent = new ServentInfo(-1, "localhost", someServentPort);
            NewNodeMessage nnm = new NewNodeMessage(AppConfig.myServentInfo, someServent, AppConfig.myServentInfo);
            nnm.setInitializer(true);
            MessageUtil.sendMessage(nnm);
        }
    }

    private int getSomeServentPort() {
        int bsPort = AppConfig.BOOTSTRAP_PORT;

        int retVal = -2;

        try {
            Socket bsSocket = new Socket("localhost", bsPort);

            PrintWriter bsWriter = new PrintWriter(bsSocket.getOutputStream());
            bsWriter.write("Hail\n" + AppConfig.myServentInfo.getListenerPort() + "\n");
            bsWriter.flush();

            Scanner bsScanner = new Scanner(bsSocket.getInputStream());
            retVal = bsScanner.nextInt();

            bsSocket.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return retVal;
    }


}
