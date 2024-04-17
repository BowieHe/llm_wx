package playtime.llm_wx.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WxRequest {

    @JsonProperty("URL")
    @JsonDeserialize(using = TrimDeserializer.class)
    private String url = "";

    @JsonProperty("ToUserName")
    @JsonDeserialize(using = TrimDeserializer.class)
    private String toUserName;

    @JsonProperty("FromUserName")
    @JsonDeserialize(using = TrimDeserializer.class)
    private String fromUserName;

    @JsonProperty("CreateTime")
    private long createTime;

    @JsonProperty("MsgType")
    private MsgType msgType;

    @JsonProperty("Content")
    @JsonDeserialize(using = TrimDeserializer.class)
    private String content;

    @JsonProperty("MsgId")
    @JsonDeserialize(using = TrimDeserializer.class)
    private String msgId;

    @JsonProperty("MsgDataId")
    @JsonDeserialize(using = TrimDeserializer.class)
    private String msgDataId;

    @JsonProperty("Idx")
    @JsonDeserialize(using = TrimDeserializer.class)
    private String idx;

    // Getters and setters
}