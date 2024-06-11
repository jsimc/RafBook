package servent.handler;

import app.AppConfig;
import app.ServentInfo;
import servent.message.CheckResult;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.TellCheckNodeMessage;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class TellCheckNodeHandler implements MessageHandler{
    private Message clientMessage;

    public TellCheckNodeHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        System.out.println("TELL CHECK NODE HANDLER");
        if(clientMessage.getMessageType() == MessageType.TELL_CHECK_NODE) {
            CheckResult checkResult = ((TellCheckNodeMessage)clientMessage).getCheckResult();
            ServentInfo nodeToCheck = ((TellCheckNodeMessage)clientMessage).getNode();
            synchronized (AppConfig.lock) {
                System.out.println("CHECK RESUULT: " + checkResult);
                if(checkResult.equals(CheckResult.FAIL)) {
                    // ako je fail odmah ga izbacuj
                    if(AppConfig.routingTable.contains(nodeToCheck.getHashId())) {
                        AppConfig.routingTable.delete(nodeToCheck);
                        AppConfig.isAlive.put(nodeToCheck, false);
//                        AppConfig.isAlive.remove(nodeToCheck);

                        AppConfig.timestampedErrorPrint("REMOVING " + nodeToCheck.getListenerPort());
                        try {
                            Socket bsSocket = new Socket("localhost", AppConfig.BOOTSTRAP_PORT);

                            PrintWriter bsWriter = new PrintWriter(bsSocket.getOutputStream());
                            bsWriter.write("Remove\n" + nodeToCheck.getListenerPort() + "\n");

                            bsWriter.flush();
                            bsSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    AppConfig.timestampedErrorPrint("ALL GOOD! Node " + nodeToCheck.getListenerPort() + " is alive!");
                }
            }
        } else {
            AppConfig.timestampedErrorPrint("TELL_CHECK_NODE Handler got something else: " + clientMessage.getMessageType());
        }
    }


}
