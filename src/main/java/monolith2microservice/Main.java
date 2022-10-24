package monolith2microservice;

import java.util.concurrent.Executor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@EnableAsync
@ComponentScan
@Configuration
@EnableAutoConfiguration
@EntityScan(basePackages = "monolith2microservice.shared.models")
@EnableJpaRepositories(basePackages = "monolith2microservice.outbound")
public class Main extends AsyncConfigurerSupport{

	public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
	}
	
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(3);
        executor.setMaxPoolSize(8);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("Monolith2Microservice-");
        executor.initialize();
        return executor;
    }

}
