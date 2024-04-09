package playtime.llm_wx.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
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

//    public YiResponse(@JsonProperty("id") String id, @JsonProperty("object") String object, @JsonProperty("created") Long created, @JsonProperty("usage") Usage usage, @JsonProperty("choices") Choice[] choices) {
//        this.id = id;
//        this.object = object;
//        this.created = created;
//        this.usage = usage;
//        this.choices = choices;
//    }

    public List<String> getMessages() {
        return Arrays.stream(this.choices)
                .flatMap(choice -> Arrays.stream(new String[]{choice.getMessage().content}))
                .collect(Collectors.toList());
    }

}
