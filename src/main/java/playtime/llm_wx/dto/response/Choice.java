package playtime.llm_wx.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Choice {
    int index;
    ChoiceMessage message;
    String finish_reason;
}