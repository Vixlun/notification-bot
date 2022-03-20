package telegram.bot.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"telegram.bot", "fun.home.findatmapi"})
public class TelegramBotWhyApplication {

	public static void main(String[] args) {
		SpringApplication.run(TelegramBotWhyApplication.class, args);
	}
}
