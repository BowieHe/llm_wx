package playtime.llm_wx.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import playtime.llm_wx.dto.response.YiResponse;
import playtime.llm_wx.service.RedisService;
import playtime.llm_wx.service.WxService;
import playtime.llm_wx.service.YiService;
import playtime.llm_wx.util.WechatPublicUtils;

import java.io.UnsupportedEncodingException;

@Slf4j
@RestController
@RequestMapping("/wechat")
public class WxController {

    @Autowired
    WxService wxService;

    @Autowired
    YiService yiService;

    @Autowired
    RedisService redisService;

    @Value("${wx.official.token}")
    private String wxToken;

    @PostMapping(value = "/event", produces = {"application/xml; charset=UTF-8"})
    @ResponseBody
    public void wechatEvent(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            wxService.handleEvent(request, response);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }

    }

    @GetMapping(value = "/message")
    @ResponseBody
    public String checkSignature(
            @RequestParam("signature") String signature,
            @RequestParam("timestamp") String timestamp,
            @RequestParam("nonce") String nonce,
            @RequestParam("echostr") String echostr) {

        log.info("------------开始校验----------");
        log.info("signature:{}", signature);
        log.info("timestamp:{}", timestamp);
        log.info("nonce:{}", nonce);
        log.info("echostr:{}", echostr);
        // 将token、timestamp、nonce三个参数进行字典序排序 并拼接为一个字符串
        String sortStr = WechatPublicUtils.sort(wxToken, timestamp, nonce);
        String mySignature = WechatPublicUtils.getSha1(sortStr);
        // 字符串加密
        log.info("密文:{}", mySignature);
        if (signature.equals(mySignature)) {
            log.info("----nonce---verifyPass--------------------------------：{}", nonce);
        } else {
            log.info("---------------verifyDown--------------------------------");
        }
        log.info("加密的echostr:{}", echostr);
        return echostr;
    }

    @PostMapping(value = "/message", produces = {"application/xml; charset=UTF-8"})
    @ResponseBody
    public String wechatMessage(HttpServletRequest request, HttpServletResponse response) {
        try {
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
