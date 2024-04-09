package playtime.llm_wx.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import playtime.llm_wx.dto.YiRequest;
import playtime.llm_wx.dto.response.YiResponse;
import playtime.llm_wx.util.RestUtil;

@Slf4j
@Service
public class YiService {

    @Value("${yi.secret}")
    private String yiSecret;

    ObjectMapper mapper = new ObjectMapper();

    public YiResponse query(String question) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", String.format("Bearer %s", yiSecret));
        headers.add("Content-Type", "application/json");

        YiRequest request = new YiRequest(question);

        RestUtil restUtil = new RestUtil();
        ResponseEntity<String> responseEntity = restUtil.post("https://api.lingyiwanwu.com/v1/chat/completions", request, String.class, headers);


        try {
            YiResponse response = mapper.readValue(responseEntity.getBody(), YiResponse.class);
            log.info("response: {}", mapper.writeValueAsString(response));

            return response;
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
        return null;

    }

}
