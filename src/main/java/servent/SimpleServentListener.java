package servent;

import app.AppConfig;
import app.Cancellable;
import servent.handler.MessageHandler;
import servent.handler.NullHandler;
import servent.message.Message;
import servent.message.util.MessageUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SimpleServentListener implements Runnable, Cancellable {
    private volatile boolean working = true;

    public SimpleServentListener() {
    }

    @Override
    public void run() {
        ServerSocket listenerSocket = null;
        try {
            // backlog argument is requested maximum number of pending connections on the socket
            listenerSocket = new ServerSocket(AppConfig.myServentInfo.getListenerPort(), 100);
            // if no data arrives within 1000ms a SocketTimeoutException will be thrown.
            listenerSocket.setSoTimeout(1000);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        while(working) {
            try {
                Message clientMessage;
                Socket clientSocket = listenerSocket.accept();
                clientMessage = MessageUtil.readMessage(clientSocket);

                MessageHandler messageHandler = new NullHandler(clientMessage);

                switch (clientMessage.getMessageType()) {

                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void stop() {
        this.working = false;
    }
}
