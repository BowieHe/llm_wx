package playtime.llm_wx.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Usage{
    int completion_tokens;
    int prompt_tokens;
    int total_tokens;
}