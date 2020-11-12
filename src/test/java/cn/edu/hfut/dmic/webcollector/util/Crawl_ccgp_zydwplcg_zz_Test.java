package cn.edu.hfut.dmic.webcollector.util;

import org.junit.Test;

import cn.edu.hfut.dmic.contentextractor.WebData;
import cn.edu.hfut.dmic.webcollector.example.CommonCrawler;

public class Crawl_ccgp_zydwplcg_zz_Test {

	// 中直批量采购招标公告
	@Test
	public void ccgp() throws Exception {
		WebData webData = new WebData();
		String baseUrl = "http://www.ccgp.gov.cn/zydwplcg/zz/zzzb/";
		//detail http://www.ccgp.gov.cn/cggg/zygg/gkzb/201908/t20190827_12770154.htm
		webData.setInitUrl(baseUrl);
		webData.setPageUrl(baseUrl + "index_");
		webData.setPageStart(1);
		webData.setPageEnd(2);
		baseUrl = "http://www.ccgp.gov.cn/cggg/zygg/gkzb";
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
