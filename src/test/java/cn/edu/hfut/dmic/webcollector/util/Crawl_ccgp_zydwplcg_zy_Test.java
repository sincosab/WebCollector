package cn.edu.hfut.dmic.webcollector.util;

import org.junit.Test;

import cn.edu.hfut.dmic.contentextractor.WebData;
import cn.edu.hfut.dmic.webcollector.example.CommonCrawler;

public class Crawl_ccgp_zydwplcg_zy_Test {

	//中央批量采购招标公告
	@Test
	public void ccgp() throws Exception {
		WebData webData = new WebData();
		String baseUrl = "http://www.ccgp.gov.cn/zydwplcg/zy/zyzb/";
		//detail http://www.ccgp.gov.cn/cggg/zygg/zbgg/202010/t20201021_15276502.htm
		webData.setInitUrl(baseUrl);
		webData.setPageUrl(baseUrl + "index_");
		webData.setPageStart(1);
		webData.setPageEnd(1);
		baseUrl="http://www.ccgp.gov.cn/cggg/zygg/zbgg";
		webData.setRegex(baseUrl+".*");
		webData.setMatchUrl(baseUrl+".*/.*");
		webData.setContent("div.vF_detail_main");
		webData.setTitle("meta[name=ArticleTitle]");
		webData.setKeyword("meta[name=ColumnKeywords]");
		webData.setPublishTime("meta[name=PubDate]");
		webData.setSite("meta[name=SiteName]");
		webData.setDomain("meta[name=SiteDomain]");
		CommonCrawler crawler = new CommonCrawler("crawl", true, webData);
		crawler.start(2);
	}
}
