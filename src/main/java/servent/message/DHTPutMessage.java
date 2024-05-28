package servent.message;

import app.ServentInfo;

public class DHTPutMessage extends BasicMessage {
    private static final long serialVersionUID = -1934709147043909222L;
    private final String value;

    public DHTPutMessage(ServentInfo sender, ServentInfo receiver, String value) {
        super(MessageType.PUT, sender, receiver, value);
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
