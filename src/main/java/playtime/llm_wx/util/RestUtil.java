package playtime.llm_wx.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
public class RestUtil {

    private final RestTemplate restTemplate = new RestTemplate();

    public static String getBody(HttpServletRequest request) {
        try (InputStream is = request.getInputStream()) {
            return IOUtils.toString(is, StandardCharsets.UTF_8);
        } catch (Exception e) {
            log.error("failed to get request body", e);
        }
        return "";
    }

    public <T> ResponseEntity<T> post(String url, Object requestBody, Class<T> responseType) {
        return post(url, requestBody, responseType, new HttpHeaders());
    }

    public <T> ResponseEntity<T> post(String url, Object requestBody, Class<T> responseType, HttpHeaders headers) {

        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.exchange(
                url,
                HttpMethod.POST,
                new HttpEntity<>(requestBody, headers),
                responseType
        );

    }

    public <T> ResponseEntity<T> get(String url, Class<T> responseType) {
        return restTemplate.getForEntity(url, responseType);
    }
}
