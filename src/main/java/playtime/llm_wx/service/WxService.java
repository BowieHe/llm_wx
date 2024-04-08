package playtime.llm_wx.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Slf4j
@Service
public class WxService {

    public static Map<String, Object> xmlToMap(String xml) throws IOException {
        XmlMapper xmlMapper = new XmlMapper();
        return xmlMapper.readValue(xml, Map.class);
    }
    public void handleEvent(HttpServletRequest request, HttpServletResponse response) {
        InputStream inputStream = null;
        XmlMapper xmlMapper = new XmlMapper();
        try {
            inputStream = request.getInputStream();
            Map<String, Object> map = xmlMapper.readValue(request.getInputStream(), Map.class);

            // openId
            String userOpenId = (String) map.get("FromUserName");
            // 微信账号
            String userName = (String) map.get("ToUserName");
            // 事件
            String event = (String) map.get("Event");
            // 区分消息类型
            String msgType = (String) map.get("MsgType");
            // 普通消息
            if ("text".equals(msgType)) {
                // todo 处理文本消息
            }
//            else if ("image".equals(msgType)) {
//            } else if ("voice".equals(msgType)) {
//            } else if ("video".equals(msgType)) {
//            }
            // 事件推送消息
            else if ("event".equals(msgType)) {
                if ("subscribe".equals(event)) {
                    log.info("用户扫码｜关注|openId:{},userName:{}", userOpenId, userName);
                    String ticket = (String) map.get("Ticket");

//                    if (StringUtils.isNotBlank(ticket)) {
//                        redisCacheManager.set(ConstantsRedisKey.ADV_WX_LOGIN_TICKET.replace("ticketId", ticket), userOpenId, 10 * 60);
//                    }
                    String mapToXml = handleEventSubscribe(map, userOpenId);
                    response.getWriter().print(mapToXml);
                    return;
                } else if ("SCAN".equals(event)) {
                    log.info("用户扫码｜登录|openId:{},userName:{}", userOpenId, userName);
                    String ticket = (String) map.get("Ticket");
//                    if (StringUtils.isNotBlank(ticket)) {
//                        redisCacheManager.set(ConstantsRedisKey.ADV_WX_LOGIN_TICKET.replace("ticketId", ticket), userOpenId, 10 * 60);
//                    }
                    // todo 业务处理
                } else if ("unsubscribe".equals(event)) {
                    log.info("用户取消关注,拜拜～,openId:{}", userOpenId);
                    // todo 取消关注 业务处理
                }
            }
            log.info("接收参数:{}", map);
        } catch (IOException e) {
            log.error("处理微信公众号请求异常：", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ioe) {
                    log.error("关闭inputStream异常：", ioe);
                }
            }
        }
    }

    /**
     * 处理 subscribe 类型的event
     *
     * @param map
     * @param userOpenId
     * @return
     */
    private String handleEventSubscribe(Map<String, Object> map, String userOpenId) {
        String resXmlStr = getReturnMsgSubscribe(map);
        log.info("用户扫码关注返回的xml:{}", resXmlStr);
        return resXmlStr;
    }


    public String getReturnMsgSubscribe(Map<String, Object> decryptMap) {

        // TODO delete
        return "";

        /*
        log.info("---开始封装xml---decryptMap:" + decryptMap.toString());
        TextMessage textMessage = new TextMessage();
        textMessage.setToUserName(decryptMap.get("FromUserName").toString());
        textMessage.setFromUserName(decryptMap.get("ToUserName").toString());
        textMessage.setCreateTime(System.currentTimeMillis());
        textMessage.setMsgType("text");
        textMessage.setContent("你好，欢迎关注XXX！\n" +
                "\n" +
                "关注XXX。立即登录PC端网址 \n" + domainname +
                " 即可完成注册！\n" +
                "\n" +
                "或，" +
                "<a href='" + domainname + "'>点击这里立即完成注册</a>");
        return getXmlString(textMessage);
        */

    }

/*
    public String getXmlString(TextMessage textMessage) {
        String xml = "";
        if (textMessage != null) {
            xml = "<xml>";
            xml += "<ToUserName><![CDATA[";
            xml += textMessage.getToUserName();
            xml += "]]></ToUserName>";
            xml += "<FromUserName><![CDATA[";
            xml += textMessage.getFromUserName();
            xml += "]]></FromUserName>";
            xml += "<CreateTime>";
            xml += textMessage.getCreateTime();
            xml += "</CreateTime>";
            xml += "<MsgType><![CDATA[";
            xml += textMessage.getMsgType();
            xml += "]]></MsgType>";
            xml += "<Content><![CDATA[";
            xml += textMessage.getContent();
            xml += "]]></Content>";
            xml += "</xml>";
        }
        return xml;
    }
    */


}
