package com.tstu.reviewdomparser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@SpringBootApplication
@EnableHystrix
public class ReviewDomParserApplication {

	public static void main(String[] args) {
		System.setProperty("webdriver.gecko.driver", "path/to/geckodriver.exe");
		SpringApplication.run(ReviewDomParserApplication.class, args);
	}

}
