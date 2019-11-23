package com.badminton.club;

import javax.persistence.EntityManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.querydsl.jpa.impl.JPAQueryFactory;

//使用QueryDSL的功能時，會依賴使用到JPAQueryFactory，而JPAQueryFactory在這裡依賴使用EntityManager，
//所以在主類中做如下配置，使得Spring自動幫我們注入EntityManager與自動管理JPAQueryFactory

@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
	@Bean
    public JPAQueryFactory jpaQuery(EntityManager entityManager) {
        return new JPAQueryFactory(entityManager);
    }
	
}
