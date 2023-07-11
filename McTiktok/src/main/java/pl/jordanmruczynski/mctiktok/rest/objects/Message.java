package pl.jordanmruczynski.mctiktok.rest.objects;

public class Message {

    public String uniqueId;
    public String userId;
    public String type;
    public MessageData data;

    public class MessageData {
        public String comment;
    }
}
