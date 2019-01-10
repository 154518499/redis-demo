package com.demo.testredis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
public class TestRedisApplication {
	public static void main(String[] args) {
		SpringApplication.run(TestRedisApplication.class, args);
	}

}

