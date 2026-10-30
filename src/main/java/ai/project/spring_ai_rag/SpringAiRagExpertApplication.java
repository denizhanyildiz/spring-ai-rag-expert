package ai.project.spring_ai_rag;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class SpringAiRagExpertApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringAiRagExpertApplication.class, args);
	}

}
