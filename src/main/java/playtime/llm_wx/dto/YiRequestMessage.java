package playtime.llm_wx.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class YiRequestMessage {

    String role = "user";
    String content;

    public YiRequestMessage(String content) {
        this.content = content;
    }
}
