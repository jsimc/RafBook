package servent.message;

import app.MyFile;
import app.ServentInfo;

import java.util.List;

public class TellViewFilesMessage extends BasicMessage{
    private static final long serialVersionUID = -71864583555507085L;

    private final List<MyFile> myFileList;

    public TellViewFilesMessage(ServentInfo sender, ServentInfo receiver, List<MyFile> myFileList) {
        super(MessageType.TELL_VIEW_FILES, sender, receiver);
        this.myFileList = myFileList;
    }

    public List<MyFile> getMyFileList() {
        return myFileList;
    }
}
