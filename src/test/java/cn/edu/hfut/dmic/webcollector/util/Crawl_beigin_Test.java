package cn.edu.hfut.dmic.webcollector.util;

import org.junit.Test;

import cn.edu.hfut.dmic.contentextractor.WebData;
import cn.edu.hfut.dmic.webcollector.example.CommonCrawler;

public class Crawl_beigin_Test {
	//北京
    @Test
    public void beijing() throws Exception{
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
		CommonCrawler crawler = new CommonCrawler("crawl", true, webData);
		crawler.start(3);
    }
}
