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

    @Autowired
    WxService wxService;

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
