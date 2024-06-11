package servent.message;

public enum MessageType {
    POISON,
    PING, PONG,
    PUT,
    ASK_GET, TELL_GET, FAIL_GET,
    NEW_NODE, TELL_NEW_NODE,
    SORRY,

    // ako je neki node sumnjicav na nekog drugog dobice ovu poruku da ga taj drugi node, pinguje da vidi jel ziv.
    // tell check node je za odgovor inicijalnom nodu -> taj nod treba da ima neki counter da vidi koliko nodeova mu je javilo
    // da je ovaj mrtav, i ako nadvlada vecina oznacava ga mrtvim i izbacuje iz svoje routingTabele.
    // TODO ubbaci caching za nove node-ove!!!
    CHECK_NODE, TELL_CHECK_NODE,
    REQ_TOKEN, REPLY_TOKEN,
    ADD_FRIEND, TELL_ADD_FRIEND,
    VIEW_FILES, TELL_VIEW_FILES,
    REMOVE_FILE
}
