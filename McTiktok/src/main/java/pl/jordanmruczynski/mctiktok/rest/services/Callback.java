package pl.jordanmruczynski.mctiktok.rest.services;

public interface Callback<T> {

    void accept(T result);

    default void onFailure(Throwable cause) {
        cause.printStackTrace();
    }
}
