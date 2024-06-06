package servent;

import app.AppConfig;
import app.Cancellable;
import app.Sleepable;
import servent.handler.*;
import servent.message.Message;
import servent.message.util.MessageUtil;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleServentListener implements Runnable, Cancellable, Sleepable {
    private volatile boolean working = true;

    private final ExecutorService threadPool = Executors.newWorkStealingPool();

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
            AppConfig.timestampedErrorPrint("Couldn't open listener socket on: " + AppConfig.myServentInfo.getListenerPort());
            System.exit(0);
        }

        while(working) {
            try {
                Message clientMessage;
                Socket clientSocket = listenerSocket.accept();
                clientMessage = MessageUtil.readMessage(clientSocket);

                MessageHandler messageHandler = new NullHandler(clientMessage);

                switch (clientMessage.getMessageType()) {
                    case PING:
                        messageHandler = new PingHandler(clientMessage);
                        break;
                    case PONG:
                        messageHandler = new PongHandler(clientMessage);
                        break;
                    case NEW_NODE:
                        messageHandler = new NewNodeHandler(clientMessage);
                        break;
                    case TELL_NEW_NODE:
                        messageHandler = new TellNewNodeHandler(clientMessage);
                        break;
                    case SORRY:
                        messageHandler = new SorryHandler(clientMessage);
                        break;
                    case PUT:
                        messageHandler = new DHTPutHandler(clientMessage);
                        break;
                    case FAIL_GET:
                        messageHandler = new DHTGetFailHandler(clientMessage);
                        break;
                    case TELL_GET:
                        messageHandler = new DHTTellGetHandler(clientMessage);
                        break;
                    case ASK_GET:
                        messageHandler = new DHTGetHandler(clientMessage);
                        break;
                    case CHECK_NODE:
                        messageHandler = new CheckNodeHandler(clientMessage);
                        break;
                    case TELL_CHECK_NODE:
                        messageHandler = new TellCheckNodeHandler(clientMessage);
                        break;
                    case REQ_TOKEN:
                        messageHandler = new RequestTokenHandler(clientMessage);
                        break;
                    case REPLY_TOKEN:
                        messageHandler = new ReplyTokenHandler(clientMessage);
                        break;
                    case ADD_FRIEND:
                        messageHandler = new AddFriendHandler(clientMessage);
                        break;
                    case TELL_ADD_FRIEND:
                        messageHandler = new TellFriendHandler(clientMessage);
                        break;
                }
                threadPool.submit(messageHandler);
            } catch (SocketTimeoutException timeoutException) {

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void stop() {
        this.working = false;
    }

    @Override
    public void sleep(int length) {
        try{
            Thread.sleep(length);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
