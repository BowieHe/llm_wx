package playtime.llm_wx.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class YiRequest {

    String model = "yi-34b-chat-0205";
    List<YiRequestMessage> messages = new ArrayList<>();
    double temperature = 0.7;

    public YiRequest(String content) {
        YiRequestMessage message = new YiRequestMessage(content);
        this.messages.add(message);
    }
}
