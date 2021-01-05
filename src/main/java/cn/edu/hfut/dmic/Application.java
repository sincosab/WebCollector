package cn.edu.hfut.dmic;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

import lombok.extern.slf4j.Slf4j;
@EnableAsync
@EnableScheduling
@Slf4j
@SpringBootApplication
//@MapperScan("cn.edu.hfut.dmic.mapper.**")
//@ComponentScan(basePackages = "cn.edu.hfut.dmic.mapper") 
public class Application {
    public static void main(String[] args) {
        log.info("-----------------开始启动----------------");
        SpringApplication.run(Application.class, args);
        log.info("-----------------启动完成----------------");
    }

}
