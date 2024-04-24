package playtime.llm_wx.service.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import playtime.llm_wx.dto.WxRequest;
import playtime.llm_wx.service.RedisService;
import playtime.llm_wx.service.YiService;
import playtime.llm_wx.util.Constant;
import playtime.llm_wx.dto.response.YiResponse;

@Component
@Slf4j
@RequiredArgsConstructor
public class LlmWxQueryConsumer {

    @Autowired
    RedisService redisService;

    @Autowired
    YiService yiService;

//    private final KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper mapper = new ObjectMapper();

    @KafkaListener(topics = Constant.KAFKA_TOPIC_LLM_WX_QUERY, groupId = Constant.KAFKA_GROUP_WX_LLM_QUERY)
    public void onMessageReceived(String message) {
        log.info("get message: {}", message);
        try {
            WxRequest request = mapper.readValue(message, WxRequest.class);
            String queryResult = redisService.getValue(request.getMsgId());
            if (!queryResult.isEmpty()) {
                log.info("already get result for MsgId: {}, with result: {}", request.getMsgId(), queryResult);
                return;
            }

            // query from Yi
            YiResponse yiResponse = yiService.query(request.getContent());
            redisService.setValue(request.getMsgId(), yiResponse.getMessages().get(0), 180);
        } catch (JsonProcessingException e) {
            log.error("error occurred while parse message: {}", message, e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            log.error("error occurred while process llm_wx_query consumer", e);
        }
    }
}
