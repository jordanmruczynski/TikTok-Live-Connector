package pl.jordanmruczynski.mctiktok.rest.requests;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import pl.jordanmruczynski.mctiktok.McTiktok;
import pl.jordanmruczynski.mctiktok.rest.objects.Message;
import pl.jordanmruczynski.mctiktok.rest.services.Callback;
import pl.jordanmruczynski.mctiktok.rest.services.ChatMessageRequestService;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

public class ChatMessageRequest implements ChatMessageRequestService {

    @Override
    public void sendRequest(String name, Callback<Map<String, Message>> callback) {
        McTiktok.getExecutorService().execute(() -> {
            try {
                URL url = new URL("http://"+ McTiktok.getMcTiktok().getServerAddress()+":"+ McTiktok.getMcTiktok().getServerPort()+"/messages/" + name);
                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpGet getRequest = new HttpGet(url.toString());
                CloseableHttpResponse response = httpClient.execute(getRequest);

                Scanner scanner = new Scanner(response.getEntity().getContent());
                StringBuilder responseBuilder = new StringBuilder();
                while (scanner.hasNext()) {
                    responseBuilder.append(scanner.nextLine());
                }

                Map<String, Message> jsonObjects = parseInput(responseBuilder.toString());
                scanner.close();
                callback.accept(jsonObjects);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public Map<String, Message> parseInput(String json) {
        Type targetClassType = new TypeToken<Map<String, Message>>(){}.getType();
        return new Gson().fromJson(json, targetClassType);
    }
}
