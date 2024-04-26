package playtime.llm_wx.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import playtime.llm_wx.dto.MsgType;
import playtime.llm_wx.dto.TextMessage;
import playtime.llm_wx.dto.WxRequest;
import playtime.llm_wx.util.Constant;
import playtime.llm_wx.util.RestUtil;
import playtime.llm_wx.util.WechatPublicUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class WxService {

    @Autowired
    RedisService redisService;

    @Autowired
    YiService yiService;

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;
    @Autowired
    private ObjectMapper jacksonObjectMapper;


    @SneakyThrows
    public String handleMessage(WxRequest request) {
        String res = "";
        for (int i = 0; i < 30; i++) {
            String response = redisService.getValue(request.getMsgId());

            if (response == null) {
                // first time query
                redisService.setValue(request.getMsgId(), "", 300);
                kafkaTemplate.send(Constant.KAFKA_TOPIC_LLM_WX_QUERY, jacksonObjectMapper.writeValueAsString(request));
            } else if (!response.isEmpty()) {
                res = response;
                break;
            }

            TimeUnit.MILLISECONDS.sleep(200); //wait for 200ms
        }

        if (res.isEmpty()) {
            log.info("query for MsgId: {} didn't finish in time, wait for next call", request.getMsgId());
            return "";
        }
        TextMessage textMessage = new TextMessage(request, res);
        return textMessage.toXmlString();
    }

    public void handleEvent(HttpServletRequest request, HttpServletResponse response) {
        InputStream inputStream = null;
        XmlMapper mapper = new XmlMapper();
//        ObjectMapper mapper = new ObjectMapper();
        try {
            inputStream = request.getInputStream();
            Map<String, Object> map = mapper.readValue(request.getInputStream(), Map.class);

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

        log.info("---开始封装xml---decryptMap:" + decryptMap.toString());
        TextMessage textMessage = new TextMessage();
        textMessage.setToUserName(decryptMap.get("FromUserName").toString());
        textMessage.setFromUserName(decryptMap.get("ToUserName").toString());
        textMessage.setCreateTime(System.currentTimeMillis());
        textMessage.setMsgType(MsgType.text);
        textMessage.setContent("你好，欢迎关注XXX！\n" +
                "\n" +
                "关注XXX。立即登录PC端网址 \n" + //domainname +
                " 即可完成注册！\n" +
                "\n" +
                "或，" +
                "<a href='" + "'>点击这里立即完成注册</a>");
        return textMessage.toXmlString();

    }

    @Value("${wx.official.appid}")
    private String appid;

    @Value("${wx.official.secret}")
    private String secret;

    public String getAccessToken() {
        String accessToken = redisService.getValue(Constant.RDS_WX_OFFICIAL_TOKEN);
        if (accessToken != null) {
            return accessToken;
        }
        String url = String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s", appid, secret);

        RestUtil restUtil = new RestUtil();
        ResponseEntity<String> responseEntity = restUtil.get(url, String.class);

        ObjectMapper mapper = new ObjectMapper();
        try {
            Map res = mapper.readValue(responseEntity.getBody(), Map.class);
            accessToken = (String) res.get("access_token");
            redisService.setValue(Constant.RDS_WX_OFFICIAL_TOKEN, accessToken, (Integer) res.get("expires_in"));
        } catch (Exception e) {
            log.error("failed to read value for access token", e);

        }
        return accessToken;
    }

    @Value("${wx.official.token}")
    private String wxToken;

    public String checkSignature(String signature, String timestamp, String nonce, String echostr) {
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
}
