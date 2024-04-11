package playtime.llm_wx.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import playtime.llm_wx.dto.response.YiResponse;
import playtime.llm_wx.service.RedisService;
import playtime.llm_wx.service.WxService;
import playtime.llm_wx.service.YiService;

import java.io.UnsupportedEncodingException;

@Slf4j
@RestController
public class WxController {

    @Autowired
    WxService wxService;

    @Autowired
    YiService yiService;

    @Autowired
    RedisService redisService;

    @PostMapping(value = "/check/signature", produces = {"application/xml; charset=UTF-8"})
    @ResponseBody
    public void wechatEvent(HttpServletRequest request, HttpServletResponse response) {
        try{
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            wxService.handleEvent(request, response);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }

    }

    @PostMapping(value = "/message", produces = {"application/xml; charset=UTF-8"})
    @ResponseBody
    public String wechatMessage(HttpServletRequest request, HttpServletResponse response) {
        try{
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            return wxService.handleMessage(request, response);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
            return "";
        }

    }

    @GetMapping(value = "/query")
    @ResponseBody
    public String query() {
        YiResponse response = yiService.query("Hi");
        // currently only return the first answer
        return response.getMessages().get(0);
    }

    @GetMapping(value = "/token")
    public String getToken() {
        return wxService.getAccessToken();
    }

    @GetMapping(value = "/test")
    public String test() {
        RestTemplate restTemplate = new RestTemplate();

        // 设置请求头
        org.springframework.http.HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer ee1e6a8612b44e9781d83b2597deea83");
        headers.set("Cookie", "acw_tc=7b39758217128029716532302eca0b0246440b5eee5fc4ca2d2bf60018f7af");

        // 设置请求体
        String requestBody = "{\"model\": \"yi-34b-chat-0205\",\"messages\": [{\"role\": \"user\", \"content\": \"Hi, who are you?\"}],\"temperature\": 0.7}";

    // 发送请求
    ResponseEntity<String> responseEntity = restTemplate.exchange(
            "https://api.lingyiwanwu.com/v1/chat/completions",
            HttpMethod.POST,
            new HttpEntity<>(requestBody, headers),
            String.class
    );

    // 处理响应
    return responseEntity.getBody();

    }
}
