package playtime.llm_wx;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LlmWxApplication {

    public static void main(String[] args) {
        Dotenv.configure()
                .directory(".env")
                .systemProperties()
                .load();

        SpringApplication.run(LlmWxApplication.class, args);
    }

}
