package servent.message;

import app.MyFile;
import app.ServentInfo;

public class DHTPutMessage extends BasicMessage {
    private static final long serialVersionUID = -1934709147043909222L;
    private final MyFile value;

    public DHTPutMessage(ServentInfo sender, ServentInfo receiver, MyFile value) {
        super(MessageType.PUT, sender, receiver, value.getFile().getName());
        this.value = value;
    }

    public MyFile getValue() {
        return value;
    }
}
