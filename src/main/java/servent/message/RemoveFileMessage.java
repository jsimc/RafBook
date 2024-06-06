package servent.message;

import app.ServentInfo;

import java.util.Set;

public class RemoveFileMessage extends BasicMessage{
    private static final long serialVersionUID = 3899837286642555636L;
    private final int fileKey;
    private final Set<ServentInfo> serventInfoSet;

    public RemoveFileMessage(ServentInfo sender, ServentInfo receiver, int fileKey, Set<ServentInfo> serventInfoSet) {
        super(MessageType.REMOVE_FILE, sender, receiver);
        this.fileKey = fileKey;
        this.serventInfoSet = serventInfoSet;
    }

    public int getFileKey() {
        return fileKey;
    }

    public Set<ServentInfo> getServentInfoSet() {
        return serventInfoSet;
    }
}
