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
    String msgType = "text";
    String content;

    public TextMessage(Map<String, Object> decryptMap, String content) {
        this.toUserName = decryptMap.get("ToUserName").toString();
        this.fromUserName = decryptMap.get("FromUserName").toString();
        this.createTime = System.currentTimeMillis();
        if (decryptMap.containsKey("msgType")) {
            this.msgType = (String) decryptMap.get("msgType");
        }
        this.content = content;
    }

    public TextMessage(WxRequest request, String content) {
        this.toUserName = request.getToUserName();
        this.fromUserName = request.getFromUserName();
        this.createTime = System.currentTimeMillis();
        this.msgType = request.getMsgType();
        this.content = content;
    }
}
