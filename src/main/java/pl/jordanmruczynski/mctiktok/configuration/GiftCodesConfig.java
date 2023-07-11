package pl.jordanmruczynski.mctiktok.configuration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class GiftCodesConfig {
    private Map<String, String> codeMap;

    public void loadCodesFromJson(String jsonFile) throws IOException {
        byte[] jsonData = Files.readAllBytes(Paths.get(jsonFile));
        ObjectMapper objectMapper = new ObjectMapper();
     //   codeMap = objectMapper.readValue(jsonData, Map.class);
        codeMap = objectMapper.readValue(jsonData, new TypeReference<Map<String, String>>() {});
    }

    public String getNumberFromCode(String code) {
        if (codeMap.containsKey(code)) {
            return String.valueOf(codeMap.get(code));
        } else {
            return "-1";
        }
    }
}
