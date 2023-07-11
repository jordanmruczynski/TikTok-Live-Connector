package pl.jordanmruczynski.mctiktok.rest.services;

public interface RequestService<String, Callback> {

    void sendRequest(String name, Callback callback);

}
