package com.vcti.ct.SRVServices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import brave.sampler.Sampler;

@SpringBootApplication
@EnableFeignClients("com.vcti.ct.SRVServices")
public class SrvServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SrvServiceApplication.class, args);
	}

	@Bean
	public Sampler defaultSampler() {
		return Sampler.ALWAYS_SAMPLE;
	}
}
