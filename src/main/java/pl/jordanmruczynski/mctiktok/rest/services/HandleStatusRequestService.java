package pl.jordanmruczynski.mctiktok.rest.services;

import pl.jordanmruczynski.mctiktok.rest.requests.StatusType;

public interface HandleStatusRequestService {

    void sendRequest(String name, StatusType statusType, Callback<String> callback);
}
