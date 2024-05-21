package servent.message;

public enum MessageType {
    POISON,
    PING, PONG,
    PUT,
    FIND_NODE, NEW_NODE, WELCOME, SORRY, FIND_VALUE, ASK_GET, TELL_GET, UPDATE,
    TELL_NEW_NODE
}
