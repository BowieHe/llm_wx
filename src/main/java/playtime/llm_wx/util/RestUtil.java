package playtime.llm_wx.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestUtil {

    private final RestTemplate restTemplate = new RestTemplate();

    public <T> ResponseEntity<T> post(String url, Object requestBody, Class<T> responseType) {
        return post(url, requestBody, responseType, new HttpHeaders());
    }

    public <T> ResponseEntity<T> post(String url, Object requestBody, Class<T> responseType, HttpHeaders headers) {

//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", String.format("Bearer %s", yiSecret));
//        headers.add("Content-Type", "application/json");

//        YiRequest request = new YiRequest(question);

        HttpEntity<Object> requestEntity = new HttpEntity<>(requestBody, headers);

        return restTemplate.exchange(
//                "https://api.lingyiwanwu.com/v1/chat/completions",
                url,
                HttpMethod.POST,
                requestEntity,
                responseType
        );

    }

    public <T> ResponseEntity<T> get(String url, Class<T> responseType) {
        return restTemplate.getForEntity(url, responseType);
    }
}
