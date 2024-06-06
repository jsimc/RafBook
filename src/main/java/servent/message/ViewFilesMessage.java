package servent.message;

import app.ServentInfo;

import java.util.Set;

public class ViewFilesMessage extends BasicMessage{
    private static final long serialVersionUID = -71864583555507085L;

    private final ServentInfo originalSender;
    private final int hashId;
    private final Set<ServentInfo> serventInfos;
    public ViewFilesMessage(ServentInfo sender, ServentInfo receiver, ServentInfo originalSender, int hashId, Set<ServentInfo> serventInfos) {
        super(MessageType.VIEW_FILES, sender, receiver);
        this.originalSender = originalSender;
        this.hashId = hashId;
        this.serventInfos = serventInfos;
    }

    public ServentInfo getOriginalSender() {
        return originalSender;
    }

    public int getHashId() {
        return hashId;
    }

    public Set<ServentInfo> getServentInfos() {
        return serventInfos;
    }
}
