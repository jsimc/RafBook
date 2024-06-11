package app.mutex;

import app.AppConfig;
import app.ServentInfo;
import app.kademlia.FindNodeAnswer;
import servent.message.ReplyTokenMessage;
import servent.message.RequestTokenMessage;
import servent.message.util.MessageUtil;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Suzuki Kasami opstim ti na mami
public class SuzukiKasami {

    private volatile boolean haveToken = false;
    private volatile boolean wantLock = false;

    private volatile boolean firstToken = false;

    private volatile Token token;

    private final Map<ServentInfo, Integer> rn = new ConcurrentHashMap<>();

    // Ovo me zbunjuje jel ja treba da se vrtim u while petlji ako nemam token?
    public synchronized void lock() {
        this.wantLock = true;
        if(!haveToken) {
            // incrementing my RN
            rn.compute(AppConfig.myServentInfo, (k, v) -> (v == null) ? 1 : v + 1);
            // sends REQUEST MESSAGE to myKClosest ?
            FindNodeAnswer findNodeAnswer = AppConfig.routingTable.findClosest(AppConfig.myServentInfo.getHashId());
            for(ServentInfo serventInfo : findNodeAnswer.getNodes()) {
                if(AppConfig.myServentInfo.equals(serventInfo)) continue;
                RequestTokenMessage requestTokenMessage = new RequestTokenMessage(AppConfig.myServentInfo, serventInfo,
                        AppConfig.myServentInfo, rn.get(AppConfig.myServentInfo), new HashSet<>(findNodeAnswer.getNodes()));
                MessageUtil.sendMessage(requestTokenMessage);
            }
        }
        while(!haveToken) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Done locking!");
    }

    public synchronized void unlock() {
        // sets LN[i] = RNi[i] to indicate that its critical section request RNi[i] has been executed
        this.wantLock = false;
        this.token.updateLN(AppConfig.myServentInfo, rn.get(AppConfig.myServentInfo));

//        For every site Sj, whose ID is not present in the token queue Q,
//        it appends its ID to Q if RNi[j] = LN[j] + 1 to indicate that site Sj has an outstanding request.
        // Ovaj deo ja radim kad god dobijem REQ_TOKEN poruku !!!

//        After above update, if the Queue Q is non-empty, it
//        pops a site ID from the Q and sends the token to site indicated by popped ID.
        sendToken();
    }

    public Map<ServentInfo, Integer> getAllSequenceNumbers() {
        return rn;
    }

    public int getSeqNumber(ServentInfo serventInfo) {
        return this.rn.getOrDefault(serventInfo, 0);
    }

    public int updateSeqNumber(ServentInfo serventInfo, int rn) {
        // sets LN[i] = RNi[i] to indicate that its critical section request RNi[i] has been executed
        return this.rn.compute(serventInfo, (serventInfo1, integer) -> rn);
    }

    public boolean isHaveToken() {
        return haveToken;
    }

    public boolean isWantLock() {
        return wantLock;
    }

    // must be used only with if haveToken == true
    public Token getToken() {
        return token;
    }

    public void setFirstHaveToken(boolean haveToken) {
        this.haveToken = haveToken;
        this.firstToken = true;
        token = new Token();
    }

    public boolean isFirstToken() {
        return firstToken;
    }

    public synchronized void sendToken() {
        if(!token.isQueueEmpty()) {
            this.haveToken = false;
            ReplyTokenMessage replyTokenMessage = new ReplyTokenMessage(
                    AppConfig.myServentInfo, token.popFromQueue(),
                    this.token);
            MessageUtil.sendMessage(replyTokenMessage);
        }
    }

    public void setWantLock(boolean wantLock) {
        this.wantLock = wantLock;
    }

    // JEL OVO TREBA DA BUDE SYNC??? NIJE MI JASNO STA TREBA STA NE TREBA DA BUDE SYNC.
    synchronized public void receiveToken(Token token) {
        this.haveToken = true;
        // setuj svoj token na ovaj sto si dobila
        this.token = token;
        // jel to to ? dobila si token...
    }
}
