package playtime.llm_wx.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
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

    @GetMapping(value = "/query")
    @ResponseBody
    public String query() {
        YiResponse response = yiService.query("Hi");
        // currently only return the first answer
        return response.getMessages().get(0);
    }

    @GetMapping(value = "/set_token")
    public String setToken() {
        redisService.setValue("test", "t", 10);
        return redisService.getValue("test");
    }
}
