package cn.edu.hfut.dmic.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.edu.hfut.dmic.service.ICrawlDataService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/contact")
public class CrawlDataController {
	//@Resource
 private ICrawlDataService dataService;
	
    @PostMapping("/insert")
  //  @Scheduled(cron = "0/5 * * * * *")
    public void test (){
	 
		try {
		 dataService.beijing();
		}
		catch (Exception e) {
			 log.info("error:",e);
		  
		}
    }

 
}
