package pl.jordanmruczynski.mctiktok.rest.services;

import pl.jordanmruczynski.mctiktok.rest.objects.Message;

import java.util.Map;

public interface ChatMessageRequestService extends RequestService<String, Callback<Map<String, Message>>> {

    void sendRequest(String name, Callback<Map<String, Message>> callback);
    Map<String, Message> parseInput(String json);

}
