package cn.edu.hfut.dmic.service.impl;


import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.edu.hfut.dmic.contentextractor.WebData;
import cn.edu.hfut.dmic.entity.CrawlData;
import cn.edu.hfut.dmic.mapper.CrawlDataMapper;
import cn.edu.hfut.dmic.service.ICrawlDataService;
import cn.edu.hfut.dmic.webcollector.example.CommonCrawler;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 
 * @since 2021-01-05
 */
@Service
@Slf4j
//@Component
//@Lazy(false)

public class CrawlDataServiceImpl extends ServiceImpl<CrawlDataMapper, CrawlData> implements ICrawlDataService {
	@Override
	@Scheduled(cron = "0/5 * * * * *")
	public synchronized void beijing() throws Exception{
		log.info("test");
	        //list https://ggzyfw.beijing.gov.cn/jyxxggjtbyqs/index.html
			//detail https://ggzyfw.beijing.gov.cn/jyxxggjtbyqs/20201112/1245169.html
	    	WebData webData = new WebData();
	    	String baseUrl="https://ggzyfw.beijing.gov.cn/jyxxggjtbyqs/";
			webData.setInitUrl(baseUrl+"index.html");
			webData.setPageUrl(baseUrl+"index_");
			webData.setPageStart(2);
			webData.setPageEnd(4);
			webData.setRegex(baseUrl+".*");
			webData.setMatchUrl(baseUrl+".*/.*");
			webData.setContent("div.div-content");
			webData.setTitle("meta[http-equiv=ArticleTitle]");
			webData.setKeyword("meta[http-equiv=ColumnKeywords]");
			webData.setPublishTime("meta[http-equiv=PubDate]");
			webData.setSite("meta[http-equiv=SiteName]");
			webData.setDomain("meta[http-equiv=SiteDomain]");
			CommonCrawler crawler = new CommonCrawler("crawl", true, webData,this.getBaseMapper());
			crawler.start(3);
	
	    }
}
