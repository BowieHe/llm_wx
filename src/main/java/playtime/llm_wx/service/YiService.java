package playtime.llm_wx.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import playtime.llm_wx.dto.YiRequest;
import playtime.llm_wx.dto.YiResponse;

@Slf4j
@Service
public class YiService {

    public String query(String question) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer ee1e6a8612b44e9781d83b2597deea83");
        headers.add("Content-Type", "application/json");

        YiRequest request = new YiRequest(question);

        ParameterizedTypeReference<YiResponse> responseType = new ParameterizedTypeReference<YiResponse>() {};


        HttpEntity<YiRequest> requestEntity = new HttpEntity<>(request, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "https://api.lingyiwanwu.com/v1/chat/completions",
                HttpMethod.POST,
                requestEntity,
                String.class
        );

        return "POST 请求响应：" + responseEntity.getBody();

    }

}
