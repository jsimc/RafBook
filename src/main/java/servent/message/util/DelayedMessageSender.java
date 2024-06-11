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
            AppConfig.isAlive.put(messageToSend.getReceiver(), false);
//            AppConfig.timestampedErrorPrint("Couldn't send message: " + messageToSend.toString());
        }
    }
}
