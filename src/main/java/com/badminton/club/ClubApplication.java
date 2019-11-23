package com.badminton.club;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
//@SpringBootApplication 實際上是了以下三個標註的集合：
//1.-----@Configuration 告訴Spring這是一個配置類，裡面的所有標註了@Bean的方法的返回值將被註冊為一個Bean
//2.-----@EnableAutoConfiguration 告訴Spring基於class path的設置、其他bean以及其他設置來為應用添加各種Bean
//3.-----@ComponentScan 告訴Spring掃描Class path下所有類來生成相應的Bean
//@EnableScheduling 告訴Spring創建一個task executor，如果我們沒有這個標註，所有@Scheduled標註都不會執行
@SpringBootApplication
@EnableScheduling
public class ClubApplication {

	public static void main(String[] args) {
		SpringApplication.run(ClubApplication.class, args);
	}

}
