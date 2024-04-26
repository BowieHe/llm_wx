package playtime.llm_wx.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import playtime.llm_wx.dto.WxRequest;
import playtime.llm_wx.service.WxService;
import playtime.llm_wx.util.RestUtil;
import playtime.llm_wx.util.WechatPublicUtils;
import playtime.llm_wx.util.XmlUtil;

import java.io.UnsupportedEncodingException;

@Slf4j
@RestController
@RequestMapping("/wechat")
public class WxController {

    final WxService wxService;

    @Autowired
    public WxController(WxService wxService) {
        this.wxService = wxService;
    }

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
        return wxService.checkSignature(signature, timestamp, nonce, echostr);
    }

    @PostMapping(value = "/message", produces = {"application/xml; charset=UTF-8"})
    @ResponseBody
    public String wechatMessage(HttpServletRequest request) {
        String requestBody = RestUtil.getBody(request);

        WxRequest wxRequest = XmlUtil.parseXml(requestBody, WxRequest.class);

        return wxService.handleMessage(wxRequest);
    }

    @GetMapping(value = "/token")
    public String getToken() {
        return wxService.getAccessToken();
    }

}
