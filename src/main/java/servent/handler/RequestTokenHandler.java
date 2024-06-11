package servent.handler;

import app.AppConfig;
import app.ServentInfo;
import app.kademlia.FindNodeAnswer;
import servent.message.*;
import servent.message.util.MessageUtil;

import java.util.HashSet;
import java.util.Set;

public class RequestTokenHandler implements MessageHandler{
    private Message clientMessage;

    public RequestTokenHandler(Message clientMessage) {
        this.clientMessage = clientMessage;
    }

    @Override
    public void run() {
        if(clientMessage.getMessageType() == MessageType.REQ_TOKEN) {
            ServentInfo originalSender = ((RequestTokenMessage)clientMessage).getOriginalSender();
            int sn = ((RequestTokenMessage)clientMessage).getUpdatedSeqNumber();
            /**
             * When a site Sj receives the request message REQUEST(i, sn) from site Si,
             * it sets RNj[i] to maximum of RNj[i] and sn i.e RNj[i] = max(RNj[i], sn).
             */
            synchronized (AppConfig.reqTokenLock) {
                AppConfig.mutex.updateSeqNumber(originalSender,
                        Math.max(AppConfig.mutex.getSeqNumber(originalSender), sn));

                // moramo pitati da li je RNj[i] == LN[i]+1
                int seqnum = AppConfig.mutex.getSeqNumber(originalSender);
                int lnForServent = AppConfig.mutex.getToken().getLNForServentInfo(originalSender);
                if(AppConfig.mutex.isHaveToken() && seqnum == lnForServent+1) {
                    // send REPLY_TOKEN_MESSAGE to original sender
                    // dokle god zelimo lock, necemo da mu saljemo.
                    // !!! Ne saljemo odmah replyTokenMessage vec u nasem tokenu(posto ga mi imamo) dodajemo u queue vrednosti ovog node-a koji ga je trqazio.

                    // ako taj servent vec nije u Q onda ga dodajemo.
                    if(!AppConfig.mutex.getToken().getQueue().contains(originalSender))
                        AppConfig.mutex.getToken().putInQueue(originalSender);

                    if(!AppConfig.mutex.isWantLock()) {
                        AppConfig.mutex.sendToken();
                    }
                } else {
                    // if not! Find kClosest (to myself) and send REQ_TOKEN_MSG with originalInfo sender
                    Set<ServentInfo> serventInfoSet = ((RequestTokenMessage) clientMessage).getServentInfoSet();
                    FindNodeAnswer findNodeAnswer = AppConfig.routingTable.findClosest(AppConfig.myServentInfo.getHashId());
                    if(!AppConfig.isSame(serventInfoSet, findNodeAnswer.getNodes())) {
                        Set<ServentInfo> newSet = new HashSet<>(serventInfoSet);
                        newSet.addAll(findNodeAnswer.getNodes());
                        for(ServentInfo serventInfo : findNodeAnswer.getNodes()) {
                            if(AppConfig.myServentInfo.equals(serventInfo)
                                    || originalSender.equals(serventInfo)
                                    || serventInfoSet.contains(serventInfo)
                            ) continue;
                            RequestTokenMessage requestTokenMessage = new RequestTokenMessage(AppConfig.myServentInfo, serventInfo,
                                    originalSender, AppConfig.mutex.getSeqNumber(originalSender), newSet);
                            MessageUtil.sendMessage(requestTokenMessage);
                        }
                    }
                }
            }
        } else {
            AppConfig.timestampedErrorPrint("REQ_TOKEN handler got: " + clientMessage);
        }
    }
}
