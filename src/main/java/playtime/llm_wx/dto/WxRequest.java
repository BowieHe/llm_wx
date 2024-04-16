package playtime.llm_wx.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WxRequest {

    @JsonProperty("ToUserName")
    private String toUserName;

    @JsonProperty("FromUserName")
    private String fromUserName;

    @JsonProperty("CreateTime")
    private long createTime;

    @JsonProperty("MsgType")
    private MsgType msgType;

    @JsonProperty("Content")
    private String content;

    @JsonProperty("MsgId")
    private String msgId;

    @JsonProperty("MsgDataId")
    private String msgDataId;

    @JsonProperty("Idx")
    private String idx;

    // Getters and setters
}