
package cn.edu.hfut.dmic.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import cn.edu.hfut.dmic.entity.CrawlData;
import cn.edu.hfut.dmic.entity.GgzyData;
import cn.edu.hfut.dmic.mapper.CrawlDataMapper;
import cn.edu.hfut.dmic.webcollector.example.CommonCrawler;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.net.OkHttpRequester;
import cn.edu.hfut.dmic.webcollector.plugin.rocks.BreadthCrawler;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

@Slf4j
public class GgzyServiceImpl extends BreadthCrawler {

	private CrawlDataMapper dataMapper;

	Map<String, GgzyData> ggzyDataMap = new HashMap<String, GgzyData>();

	int pageCount = 0;

	public GgzyServiceImpl(final String crawlPath, boolean autoParse, CrawlDataMapper mapper, String url) {
		super(crawlPath, autoParse);
		dataMapper = mapper;
		addSeed(new CrawlDatum(url));
		setRequester(new OkHttpRequester() {
			@Override
			public Request.Builder createRequestBuilder(CrawlDatum crawlDatum) {
				Request.Builder requestBuilder = super.createRequestBuilder(crawlDatum);
				RequestBody requestBody;
				requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("", "").build();
				return requestBuilder.post(requestBody);
			}
		});

	}

	@Override
	public void visit(Page page, CrawlDatums next) {
		if (page != null && !page.url().contains("information/html")) {
			JsonObject jsonObject = page.jsonObject();
			JsonArray jsonArray = (JsonArray) jsonObject.get("data");
			pageCount = Integer.parseInt(jsonObject.get("ttlpage").getAsString());
			if (jsonArray != null) {
				for (int i = 0; i < jsonArray.size(); i++) {
					JsonObject json = jsonArray.get(i).getAsJsonObject();
					String url = json.get("url").getAsString();
					url = url.replace("html/a/", "html/b/");
					GgzyData ggzyData = new GgzyData();
					ggzyData.setDistrictShow(json.get("districtShow").getAsString());
					ggzyData.setClassifyShow(json.get("classifyShow").getAsString());
					ggzyData.setStageShow(json.get("stageShow").getAsString());
					ggzyData.setPlatformName(json.get("platformName").getAsString());
					ggzyData.setTradeShow(json.get("tradeShow").getAsString());
					ggzyDataMap.put(url, ggzyData);
					next.addAndReturn(url).meta("method", "POST");
				}
				try {
					// Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			// log.info("JSON信息：" + jsonObject);
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
			check = CommonCrawler.checkTitle(title);
			if (check) {
				content = CommonCrawler.getContent(page, featureContent);
				check = CommonCrawler.checkContent(content);
			}
			if (check) {
				publishTime = CommonCrawler.getPublishTime(page, featurePublish);
				check = CommonCrawler.checkPublishTime(publishTime);
			}
			check = true;
			if (check) {
				CrawlData crawlData = new CrawlData();
				crawlData.setTitle(title);
				crawlData.setContent(content);
				String url = page.crawlDatum().url();
				crawlData.setUrl(url);
				crawlData.setPublishTime(publishTime);
				GgzyData ggzyData = ggzyDataMap.get(url);
				crawlData.setProvince(ggzyData.getDistrictShow());
				crawlData.setSiteName(ggzyData.getPlatformName());
				crawlData.setInformationType(ggzyData.getStageShow());
				crawlData.setChannel(ggzyData.getClassifyShow());
				crawlData.setIndustry(ggzyData.getTradeShow());
				// crawlData.setId(-1L);
				// log.info("抓取数据入库:" + JSON.toJSONString(crawlData));
				if (crawlData != null) {
					dataMapper.insert(crawlData);
				}
			}
		}
		return;
	}

	public GgzyServiceImpl(final String crawlPath, boolean autoParse, CrawlDataMapper mapper, List<String> listUrl) {
		super(crawlPath, autoParse);
		dataMapper = mapper;
		for (int i = 0; i < listUrl.size(); i++) {
			addSeed(new CrawlDatum(listUrl.get(i)));
		}
		setRequester(new OkHttpRequester() {
			@Override
			public Request.Builder createRequestBuilder(CrawlDatum crawlDatum) {
				Request.Builder requestBuilder = super.createRequestBuilder(crawlDatum);
				RequestBody requestBody;
				requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart("", "").build();
				return requestBuilder.post(requestBody);
			}
		});
	}

	public int getPageCount() throws Exception {
		return pageCount;
	}

	public static void main(String[] args) throws Exception {
		// GgzyServiceImpl crawler = new GgzyServiceImpl("json_crawler", true,);
		// crawler.start(2);
	}

}
