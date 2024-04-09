package playtime.llm_wx.dto.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class YiResponseTest {

    @Test
    public void testConstructor() throws Exception {
        // 准备测试数据
        String jsonString = "{\"id\":\"cmpl-574d0e81\",\"object\":\"chat.completion\",\"created\":2808487,\"model\":\"yi-34b-chat-0205\",\"usage\":{\"completion_tokens\":8,\"prompt_tokens\":10,\"total_tokens\":18},\"choices\":[{\"index\":0,\"message\":{\"role\":\"assistant\",\"content\":\"你好！有什么可以帮助你的吗？\"},\"finish_reason\":\"stop\"}]}";

        // 使用 ObjectMapper 将 JSON 字符串转换为 YiResponse 对象
        ObjectMapper objectMapper = new ObjectMapper();
        YiResponse yiResponse = objectMapper.readValue(jsonString, YiResponse.class);

        // 验证构造器是否正确设置了对象的字段
        assertEquals("cmpl-574d0e81", yiResponse.getId());

        List<String> messages = yiResponse.getMessages();
        assertEquals(1, messages.size());
        assertEquals("你好！有什么可以帮助你的吗？", messages.get(0));
    }

}