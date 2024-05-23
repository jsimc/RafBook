package servent.message.util;

import app.AppConfig;
import servent.message.Message;
import servent.message.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class DelayedMessageSender implements Runnable {

    private Message messageToSend;

    public DelayedMessageSender(Message messageToSend) {
        this.messageToSend = messageToSend;
    }

    @Override
    public void run() {
        try {
            Thread.sleep((long)(Math.random() * 1000) + 500);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        if (MessageUtil.MESSAGE_UTIL_PRINTING) {
            AppConfig.timestampedStandardPrint("Sending message " + messageToSend);
        }

        try {
            Socket sendSocket = new Socket(messageToSend.getReceiverIpAddress(), messageToSend.getReceiverPort());

            ObjectOutputStream oos = new ObjectOutputStream(sendSocket.getOutputStream());
            oos.writeObject(messageToSend);
            oos.flush();

            sendSocket.close();
        } catch (IOException e) {
            //TODO ako je cvor neaktivan onda ce ovde da pukne samo moras da proveris da li je PING poruka.
            if(messageToSend.getMessageType() == MessageType.PING) {
                // ako je poruka PING i nije uspela da se posalje znaci da taj cvor ne radi.
                // pretpostavicu da ja pingujem samo moje
//                AppConfig.routingTable.delete();
            }
            AppConfig.timestampedErrorPrint("Couldn't send message: " + messageToSend.toString());
        }
    }
}
