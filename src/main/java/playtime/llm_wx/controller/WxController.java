package playtime.llm_wx.controller;

import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import playtime.llm_wx.service.WxService;
import playtime.llm_wx.service.YiService;

import java.io.UnsupportedEncodingException;

@RestController
public class WxController {

    @Autowired
    WxService wxService;

    @Autowired
    YiService yiService;


    @PostMapping(value = "/check/signature", produces = {"application/xml; charset=UTF-8"})
    @ResponseBody
    public void wechatEvent(HttpServletRequest request, HttpServletResponse response) {
        try{
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            wxService.handleEvent(request, response);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    @GetMapping(value = "/query")
    @ResponseBody
    public String query() {
        return yiService.query("Hi");
    }
}
