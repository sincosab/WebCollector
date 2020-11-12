package cn.edu.hfut.dmic.webcollector.util;

import org.junit.Test;

import cn.edu.hfut.dmic.contentextractor.WebData;
import cn.edu.hfut.dmic.webcollector.example.EadylyCrawler;

public class Crawl_ccgp_eadylynotice_Test {

	//征求意见
	@Test
	public void ccgp() throws Exception {
		WebData webData = new WebData();
		String baseUrl = "http://www.ccgp.gov.cn/eadylynotice/";
		//detail http://www.ccgp.gov.cn/eadylynotice/202011/t20201110_15397681.htm
	    //www.ccgp.gov.cn/eadylynotice/202008/t20200804_14775550.htm
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
		EadylyCrawler crawler = new EadylyCrawler("crawl", true, webData);
		crawler.start(3);
	}
}
