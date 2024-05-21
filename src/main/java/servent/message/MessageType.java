package servent.message;

public enum MessageType {
    POISON,
    PING, PONG,
    PUT, ASK_GET, TELL_GET, FAIL_GET,
    FIND_NODE, NEW_NODE, WELCOME, SORRY, FIND_VALUE, UPDATE,
    TELL_NEW_NODE
}
