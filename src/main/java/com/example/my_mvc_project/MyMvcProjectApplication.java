package com.example.my_mvc_project;

import com.example.my_mvc_project.configurations.SecurityConfiguration;
import com.example.my_mvc_project.services.ImageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

@Slf4j
@AllArgsConstructor
@SpringBootApplication
@EnableScheduling
public class MyMvcProjectApplication {
	private final ImageService imageService;

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("Asia/Tashkent"));
		SpringApplication.run(MyMvcProjectApplication.class, args);
	}

	@Bean
	public TaskExecutor taskExecutor(){
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setMaxPoolSize(120);
		taskExecutor.setQueueCapacity(150);
		taskExecutor.setCorePoolSize(100);
		taskExecutor.initialize();
		return taskExecutor;
	}
	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	@Scheduled(cron = "0 * * * * *")
	public void deleteImages(){
		Iterator<Map.Entry<String, LocalDateTime>> iterator =
				ImageService.cachedImages.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, LocalDateTime> entry = iterator.next();
			if (entry.getValue().isBefore(LocalDateTime.now())) {
				iterator.remove();
				imageService.delete(entry.getKey());
				System.out.println(entry.getKey()+" was deleted");
			}
		}
	}
	@Scheduled(cron = "0 * * * * *")
	public void deleteIpAddresses(){
		SecurityConfiguration.ipAddresses.clear();
	}

//	@Bean
	public Connector redirectConnector () {
		Connector connector = new Connector ("org.apache.coyote.http11.Http11NioProtocol");
		connector.setScheme ("http");
		connector.setPort (8080);
		connector.setSecure (false);
		connector.setRedirectPort (8443);
		return connector;
	}

}
