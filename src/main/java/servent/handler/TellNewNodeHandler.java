package servent.handler;

import app.AppConfig;
import app.ServentInfo;
import app.kademlia.FindNodeAnswer;
import servent.message.Message;
import servent.message.MessageType;
import servent.message.NewNodeMessage;
import servent.message.TellNewNodeMessage;
import servent.message.util.MessageUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;

public class TellNewNodeHandler implements MessageHandler {
    private Message clientMessage;

    public TellNewNodeHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }
    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.TELL_NEW_NODE) {
            TellNewNodeMessage tellNewNodeMessage = (TellNewNodeMessage)clientMessage;
            // znaci mi smo joined node i dobijamo informaciju o prvom node-u kod nas, treba da ga ubacimo u svoju tabelu i onda preko njega da trazimo dalje druge node-ove.
            FindNodeAnswer findNodeAnswer = tellNewNodeMessage.getFindNodeAnswer();

            if(tellNewNodeMessage.isInit()) {
                // da obavestimo bootstrap da smo init!
                try {
                    Socket bsSocket = new Socket("localhost", AppConfig.BOOTSTRAP_PORT);

                    PrintWriter bsWriter = new PrintWriter(bsSocket.getOutputStream());
                    bsWriter.write("New\n" + AppConfig.myServentInfo.getListenerPort() + "\n");

                    bsWriter.flush();
                    bsSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

//            AppConfig.timestampedStandardPrint("NODE ANSWER from: " + tellNewNodeMessage.getSenderPort());
//            AppConfig.timestampedStandardPrint(String.valueOf(findNodeAnswer));

            // sve sto nam je prvi vratio ubacujemo kod sebe u routing table i saljemo i njima newNodeMessage
            findNodeAnswer.getNodes().forEach(serventInfo -> {
                if (serventInfo.getHashId() == AppConfig.myServentInfo.getHashId()){
                    return;
                }
                // ako je sender jedan od kClosest (poslao je samog sebe)
                // onda samo uradi update. ne moras opet da mu saljes.
                if(serventInfo.getHashId() == tellNewNodeMessage.getSender().getHashId()) AppConfig.routingTable.update(serventInfo);

                int isNew = AppConfig.routingTable.update(serventInfo);
                if(isNew == 0) {
                    NewNodeMessage newNodeMessage = new NewNodeMessage(AppConfig.myServentInfo, serventInfo, AppConfig.myServentInfo);
                    MessageUtil.sendMessage(newNodeMessage);
                }
            });
        } else {
            AppConfig.timestampedErrorPrint("TELL_NEW_NODE handler got something that is not new node message.");
        }
    }
}
