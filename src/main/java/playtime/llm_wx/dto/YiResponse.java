package playtime.llm_wx.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class YiResponse {

    String id;
    String object;
    Long created;
    String model;
    Usage usage;
    Choice[] choices;

    public List<String> getMessages() {
        return Arrays.stream(this.choices)
                .flatMap(choice -> Arrays.stream(new String[]{choice.getMessage().content}))
                .collect(Collectors.toList());
    }
}

@Setter
@Getter
@AllArgsConstructor
class Usage{
    int completion_tokens;
    int prompt_tokens;
    int total_tokens;
}

@Setter
@Getter
@AllArgsConstructor
class Choice {
    int index;
    ChoiceMessage message;
    String finishReason;
}

@Setter
@Getter
@AllArgsConstructor
class ChoiceMessage {
    String role;
    String content;
}