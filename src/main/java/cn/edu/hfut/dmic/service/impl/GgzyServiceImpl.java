
package cn.edu.hfut.dmic.service.impl;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cn.edu.hfut.dmic.entity.CrawlData;
import cn.edu.hfut.dmic.entity.CrawlSite;
import cn.edu.hfut.dmic.entity.GgzyData;
import cn.edu.hfut.dmic.entity.GgzyResult;
import cn.edu.hfut.dmic.mapper.CrawlDataMapper;
import cn.edu.hfut.dmic.mapper.CrawlSiteMapper;
import cn.edu.hfut.dmic.service.ICrawlSiteService;
import cn.edu.hfut.dmic.service.IGgzyService;
import cn.edu.hfut.dmic.webcollector.example.CommonCrawler;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.net.OkHttpRequester;
import cn.edu.hfut.dmic.webcollector.plugin.rocks.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.util.ExceptionUtils;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

//全国公共资源交易平台
//@Service
@Slf4j
//@Component
//@Lazy(false)
public class GgzyServiceImpl extends BreadthCrawler {

	private CrawlDataMapper dataMapper;

	Map<String, GgzyData> ggzyDataMap = new HashMap<String, GgzyData>();

	public synchronized void getSite(CrawlDataMapper mapper) throws Exception {

		// GgzyServiceImpl crawler = new GgzyServiceImpl("json_crawler", true);
		// crawler.start(2);
	}

	public GgzyServiceImpl(final String crawlPath, boolean autoParse, CrawlDataMapper mapper) {
		super(crawlPath, autoParse);
		dataMapper = mapper;
		// addSeed(new
		// CrawlDatum("http://deal.ggzy.gov.cn/ds/deal/dealList_find.jsp?TIMEBEGIN_SHOW=2021-01-28&TIMEEND_SHOW=2021-02-06&TIMEBEGIN=2021-01-28&TIMEEND=2021-02-06&SOURCE_TYPE=1&DEAL_TIME=02&DEAL_CLASSIFY=00&DEAL_STAGE=0000&DEAL_PROVINCE=0&DEAL_CITY=0&DEAL_PLATFORM=0&BID_PLATFORM=0&DEAL_TRADE=0&isShowAll=1&PAGENUMBER=1&FINDTXT=")
		// .meta("method", "POST"));

		// addSeed("http://deal.ggzy.gov.cn/");
		addRegex("http://deal.ggzy.gov.cn/information/html/.*");
		// addRegex("-.*#.*");

		addSeed(new CrawlDatum(
				"http://deal.ggzy.gov.cn/ds/deal/dealList_find.jsp?TIMEBEGIN_SHOW=2021-01-28&TIMEEND_SHOW=2021-02-06&TIMEBEGIN=2021-01-28&TIMEEND=2021-02-06&SOURCE_TYPE=1&DEAL_TIME=02&DEAL_CLASSIFY=00&DEAL_STAGE=0000&DEAL_PROVINCE=0&DEAL_CITY=0&DEAL_PLATFORM=0&BID_PLATFORM=0&DEAL_TRADE=0&isShowAll=1"
						+ "&PAGENUMBER=2&FINDTXT=").meta("method", "POST"));
		addRegex(".*");
		setRequester(new OkHttpRequester() {
			@Override
			public Request.Builder createRequestBuilder(CrawlDatum crawlDatum) {
				Request.Builder requestBuilder = super.createRequestBuilder(crawlDatum);

				// String method = crawlDatum.meta("method");
				// if (method.equals("POST")) {
				RequestBody requestBody;
				requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
						.addFormDataPart("TIMEBEGIN_SHOW", "2021-01-28").build();

				return requestBuilder.post(requestBody);
				// }
				// ExceptionUtils.fail("wrong method: " + method);
				// return null;
			}
		});

	}

	@Override
	public void visit(Page page, CrawlDatums next) {
		if (page != null && !page.url().contains("information/html")) {
			System.out.println("page.url()信息：" + page.url());
			JsonObject jsonObject = page.jsonObject();
			JsonArray jsonArray = (JsonArray) jsonObject.get("data");
			if (jsonArray != null) {
				for (int i = 0; i < jsonArray.size(); i++) {
					JsonObject json = jsonArray.get(i).getAsJsonObject();
					String url = json.get("url").getAsString();
					url = url.replace("html/a/", "html/b/");
					System.out.println("url信息：" + url);
					GgzyData ggzyData = new GgzyData();
					ggzyData.setDistrictShow(json.get("districtShow").getAsString());
					ggzyData.setClassifyShow(json.get("classifyShow").getAsString());
					ggzyData.setStageShow(json.get("stageShow").getAsString());
					ggzyData.setPlatformName(json.get("platformName").getAsString());
					ggzyDataMap.put(url, ggzyData);
					next.addAndReturn(url).meta("method", "POST");
				}
			}
			System.out.println("JSON信息：" + jsonObject);
		}
		boolean succeed = false;
		boolean check = false;
		succeed = page != null && page.url().contains("information/html");
		if (succeed) {
			String title;
			String content = null;
			String publishTime = null;
			String featureTitle = "div.detail h4.h4_o";
			String featureContent = "div#mycontent";
			String featurePublish = ".detail .p_o span";

			title = CommonCrawler.getTitle(page, featureTitle);
			System.out.println("url信息：" + page.url());
			System.out.println("title信息：" + title);
			check = CommonCrawler.checkTitle(title);
			if (check) {
				content = CommonCrawler.getContent(page, featureContent);
				check = CommonCrawler.checkContent(content);
			}
			System.out.println("content信息：" + content);
			if (check) {
				publishTime = CommonCrawler.getPublishTime(page, featurePublish);
				check = CommonCrawler.checkPublishTime(publishTime);
			}
			System.out.println("publishTime信息：" + publishTime);
			if (check) {
				CrawlData crawlData = new CrawlData();
				crawlData.setTitle(title);
				crawlData.setContent(content);
				String url = page.crawlDatum().url();
				crawlData.setUrl(url);
				crawlData.setPublish(publishTime);
				GgzyData ggzyData = ggzyDataMap.get(url);
				crawlData.setProvince(ggzyData.getDistrictShow());
				crawlData.setSiteName(ggzyData.getPlatformName());
				crawlData.setType(ggzyData.getStageShow());
				crawlData.setChannel(ggzyData.getClassifyShow());
				// crawlData.setId(-1L);
				log.info("抓取数据入库:" + JSON.toJSONString(crawlData));
				if (crawlData != null) {
					dataMapper.insert(crawlData);
				}
			}
		}

		return;
	}

	public String getCommon(Page page, String features) {
		String str = "";
		// 多个特征用，分割
		try {
			if (StringUtils.isNotBlank(features)) {
				String featuresArray[] = features.split(",");
				for (int i = 0; i < featuresArray.length; i++) {
					if (page.select(featuresArray[i]).first() != null) {
						str = page.select(featuresArray[i]).first().text();
						if (StringUtils.isNotBlank(str)) {
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			log.error("getCommon 解析内容异常:" + e.getLocalizedMessage(), e);
		}
		return str;
	}

	public static void main(String[] args) throws Exception {

		// GgzyServiceImpl crawler = new GgzyServiceImpl("json_crawler", true,);
		// crawler.start(2);
	}

}
