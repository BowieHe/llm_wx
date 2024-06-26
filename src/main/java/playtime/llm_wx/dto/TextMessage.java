package playtime.llm_wx.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TextMessage {

    String toUserName;
    String fromUserName;
    Long createTime;
    MsgType msgType = MsgType.text;
    String content;

    public TextMessage(Map<String, Object> decryptMap, String content) {
        this.toUserName = decryptMap.get("ToUserName").toString();
        this.fromUserName = decryptMap.get("FromUserName").toString();
        this.createTime = System.currentTimeMillis();
        if (decryptMap.containsKey("msgType")) {
            this.msgType = MsgType.getMsgType((String) decryptMap.get("msgType"));
        }
        this.content = content;
    }

    public TextMessage(WxRequest request, String content) {
        this.toUserName = request.getFromUserName();
        this.fromUserName = request.getToUserName();
        this.createTime = System.currentTimeMillis();
        this.msgType = request.getMsgType();
        this.content = content;
    }

    public String toXmlString() {
        String xml = "<xml>";
        xml += "<ToUserName><![CDATA[";
        xml += this.getToUserName();
        xml += "]]></ToUserName>";
        xml += "<FromUserName><![CDATA[";
        xml += this.getFromUserName();
        xml += "]]></FromUserName>";
        xml += "<CreateTime>";
        xml += this.getCreateTime();
        xml += "</CreateTime>";
        xml += "<MsgType><![CDATA[";
        xml += this.getMsgType().getName();
        xml += "]]></MsgType>";
        xml += "<Content><![CDATA[";
        xml += this.getContent();
        xml += "]]></Content>";
        xml += "</xml>";
        return xml;
    }
}
