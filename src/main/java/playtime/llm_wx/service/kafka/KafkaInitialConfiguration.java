package playtime.llm_wx.service.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import playtime.llm_wx.util.Constant;

@Configuration
public class KafkaInitialConfiguration {

    @Bean
    public NewTopic initialTopic() {
        return new NewTopic(Constant.KAFKA_TOPIC_LLM_WX_QUERY, 1, (short) 1);
    }

}
