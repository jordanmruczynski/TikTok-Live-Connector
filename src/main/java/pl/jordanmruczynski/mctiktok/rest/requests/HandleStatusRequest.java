package pl.jordanmruczynski.mctiktok.rest.requests;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import pl.jordanmruczynski.mctiktok.McTiktok;
import pl.jordanmruczynski.mctiktok.rest.services.Callback;
import pl.jordanmruczynski.mctiktok.rest.services.HandleStatusRequestService;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

public class HandleStatusRequest implements HandleStatusRequestService {

    @Override
    public void sendRequest(String name, StatusType statusType, Callback<String> callback) {
        McTiktok.getExecutorService().execute(() -> {
            try {
                URL url = new URL("http://"+ McTiktok.getMcTiktok().getServerAddress()+":"+ McTiktok.getMcTiktok().getServerPort()+"/" + statusType.name().toLowerCase() + "/" + name);
                CloseableHttpClient httpClient = HttpClients.createDefault();
                HttpGet getRequest = new HttpGet(url.toString());
                CloseableHttpResponse response = httpClient.execute(getRequest);

                Scanner scanner = new Scanner(response.getEntity().getContent());
                StringBuilder responseBuilder = new StringBuilder();
                while (scanner.hasNext()) {
                    responseBuilder.append(scanner.nextLine());
                }

                scanner.close();
                callback.accept(responseBuilder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }
}
