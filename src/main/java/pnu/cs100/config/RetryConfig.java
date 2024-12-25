package pnu.cs100.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@EnableRetry
public class RetryConfig {
    // 추가적인 설정이 필요하다면 여기에 작성
}