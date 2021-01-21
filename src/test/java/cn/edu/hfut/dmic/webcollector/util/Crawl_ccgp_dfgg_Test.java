package cn.edu.hfut.dmic.webcollector.util;

import org.junit.Test;

import cn.edu.hfut.dmic.contentextractor.WebData;
import cn.edu.hfut.dmic.webcollector.example.CommonCrawler;

public class Crawl_ccgp_dfgg_Test {

	// 国家地方公告
	@Test
	public void ccgp() throws Exception {
		WebData webData = new WebData();
		//list http://www.ccgp.gov.cn/cggg/dfgg/
		//detail http://www.ccgp.gov.cn/cggg/dfgg/jzxcs/202011/t20201112_15413191.htm
		String baseUrl = "http://www.ccgp.gov.cn/cggg/dfgg/";
		webData.setInitUrl(baseUrl);
		webData.setPageUrl(baseUrl + "index_");
		webData.setPageStart(1);
		webData.setPageEnd(2);
		webData.setRegex(baseUrl+".*");
		webData.setMatchUrl(baseUrl+".*/.*");
		webData.setContent("div.vF_detail_main");
		webData.setTitle("meta[name=ArticleTitle]");
		webData.setKeyword("meta[name=ColumnKeywords]");
		webData.setPublishTime("meta[name=PubDate]");
		webData.setSite("meta[name=SiteName]");
		webData.setDomain("meta[name=SiteDomain]");
		CommonCrawler crawler = new CommonCrawler("crawl", true, webData);
		crawler.start(3);
	}
}
