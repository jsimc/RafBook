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

        if (MessageUtil.MESSAGE_UTIL_PRINTING && !messageToSend.getMessageType().equals(MessageType.PONG) && !messageToSend.getMessageType().equals(MessageType.PING)) {
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
            // zapravo ovo bi trebalo uraditi koja god da je poruka u pitanju! Ako ne mozes da je posaljes obrisi ga iz routing table
            // i mogao bi da azuriras (republish) svojih values.
//            AppConfig.routingTable.delete(messageToSend.getReceiver());
            AppConfig.isAlive.put(messageToSend.getReceiver(), false);
            // TODO treba uraditi republishing svih values.
            AppConfig.timestampedErrorPrint("Couldn't send message: " + messageToSend.toString());
        }
    }
}
