package servent.message;

import app.ServentInfo;

public class SorryMessage extends BasicMessage {

	private static final long serialVersionUID = 8866446621366084210L;

	public SorryMessage(ServentInfo senderPort, ServentInfo receiverPort) {
		super(MessageType.SORRY, senderPort, receiverPort);
	}
}
