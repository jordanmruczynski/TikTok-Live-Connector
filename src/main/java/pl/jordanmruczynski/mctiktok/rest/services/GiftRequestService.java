package pl.jordanmruczynski.mctiktok.rest.services;

import pl.jordanmruczynski.mctiktok.rest.objects.Gift;

import java.util.Map;

public interface GiftRequestService extends RequestService<String, Callback<Map<String, Gift>>> {

    void sendRequest(String name, Callback<Map<String, Gift>> callback);
    Map<String, Gift> parseInput(String json);

}
