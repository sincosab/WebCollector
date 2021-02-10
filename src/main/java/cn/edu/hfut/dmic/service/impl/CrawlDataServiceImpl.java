package cn.edu.hfut.dmic.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.edu.hfut.dmic.contentextractor.WebData;
import cn.edu.hfut.dmic.entity.CrawlData;
import cn.edu.hfut.dmic.entity.CrawlSite;
import cn.edu.hfut.dmic.mapper.CrawlDataMapper;
import cn.edu.hfut.dmic.mapper.CrawlSiteMapper;
import cn.edu.hfut.dmic.service.ICrawlDataService;
import cn.edu.hfut.dmic.webcollector.example.CommonCrawler;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author
 * @since 2021-01-05
 */
@Service
@Slf4j
public class CrawlDataServiceImpl extends ServiceImpl<CrawlDataMapper, CrawlData> implements ICrawlDataService {
	@Resource
	private CrawlSiteMapper siteMapper;

	@Resource
	private CrawlDataMapper dataMapper;

	// 获取今天数据
	//@PostConstruct
	public void getTodayGgzySite() throws Exception {
		int beforeDate = 0;
		String dealTime = "01";
		String dealClissfy = "00";
		String dealStage = "0000";
		String sourceType = "1";
		List<String> crawlDate = getCrawlDate(beforeDate);
		getGgzyData(dealTime, crawlDate, sourceType, dealClissfy, dealStage);
	}

	// 获取昨天数据
	// @PostConstruct
	public void getLastDayGgzySite() throws Exception {
		String dealTime = "06";
		String dealClissfy = "00";
		String dealStage = "0000";
		String sourceType = "1";
		List<String> crawlDate = getLastDate();
		getGgzyData(dealTime, crawlDate, sourceType, dealClissfy, dealStage);
	}

	// 获取最近几天数据
// @PostConstruct
	public void getRecentDayGgzySite() throws Exception {
		String dealTime = "02";
		String dealClissfy = "00";
		String dealStage = "0000";
		String sourceType = "1";
		List<String> crawlDate= new 	ArrayList<String>();
		crawlDate.add("2021-02-09");//end
		crawlDate.add("2021-01-31");//begin
		getGgzyData(dealTime, crawlDate, sourceType, dealClissfy, dealStage);
	}

	// 获取央企数据
 @PostConstruct
	public void getYqGgzySite() throws Exception {
		int beforeDate = 0;
		String dealTime = "02";
		String dealClissfy = "01";
		String dealStage = "0100";
		String sourceType = "2";
		beforeDate = 9;
		List<String> crawlDate = getCrawlDate(beforeDate);
		getGgzyData(dealTime, crawlDate, sourceType, dealClissfy, dealStage);
	}

	// 获取数据
	public void getGgzyData(String dealTime, List<String> crawlDate, String sourceType, String dealClissfy,
			String dealStage) throws Exception {
		int siteId = 0;
		initData(siteId);
		String begin = crawlDate.get(crawlDate.size() - 1);
		String end = crawlDate.get(0);
		/*
		 * http://deal.ggzy.gov.cn/ds/deal/dealList_find.jsp?TIMEBEGIN_SHOW=2021-01-28&
		 * TIMEEND_SHOW=2021-02-06&TIMEBEGIN=2021-01-28&TIMEEND=2021-02-06&SOURCE_TYPE=1
		 * &DEAL_TIME=02&DEAL_CLASSIFY=00&DEAL_STAGE=0000&DEAL_PROVINCE=0&DEAL_CITY=0&
		 * DEAL_PLATFORM=0&BID_PLATFORM=0&DEAL_TRADE=0&isShowAll=1" +
		 * "&PAGENUMBER=2&FINDTXT=
		 */
		String url = "http://deal.ggzy.gov.cn/ds/deal/dealList_find.jsp?" +
		      "TIMEBEGIN_SHOW=" + begin +
		       "&TIMEEND_SHOW="+ end +
		        "&TIMEBEGIN=" + begin +
				"&TIMEEND=" + end +
				"&SOURCE_TYPE=" + sourceType + 
				"&DEAL_TIME="+ dealTime +
				"&DEAL_CLASSIFY=" + dealClissfy +
				"&DEAL_STAGE=" + dealStage
				+ "&DEAL_PROVINCE=0&DEAL_CITY=0&DEAL_PLATFORM=0&BID_PLATFORM=0" + "&DEAL_TRADE=0&isShowAll=1"
				+ "&PAGENUMBER=1&FINDTXT=";
		log.info("第1次分页抓取");
		GgzyServiceImpl crawler = new GgzyServiceImpl("crawl-ggzy", true, dataMapper, url);
		crawler.start(2);
		/* 开始阻塞 */
		while (crawler.isResumable()) {
		}
		int pageCount = crawler.getPageCount() + 1;
		log.info("抓取总页数：" + pageCount);
		Random r = new Random();
		int a = 100;
		int b = 200;
		int c = r.nextInt(b - a + 1) + a;
		for (int i = 2; i < pageCount; i++) {
			try {
				log.info("第" + i + "次分页抓取,已经抓取总数：" + (i - 1) * 20);
				Thread.sleep(c);
				String pages = url.replace("PAGENUMBER=1", "PAGENUMBER=" + i);
				GgzyServiceImpl crawl = new GgzyServiceImpl("crawl-ggzy", true, dataMapper, pages);
				crawl.start(2);
				while (crawl.isResumable()) {
				}
				crawl.stop();
			} catch (Exception e) {
				log.info("分页抓取错误", e);
			}
		}
	}

	// @Scheduled(cron = "0 0 */8 * * ?")
	// @PostConstruct
	public synchronized void getSite() throws Exception {
		log.info("初始化数据开始");
		int siteId = 0;
		int beforeMonth = 1;
		initData(siteId);
		List<String> crawlMonth = getCrawlMonth(beforeMonth);
		executeSite(crawlMonth);
	}

	public synchronized void executeSite(List<String> crawlMonth) throws Exception {
		log.info("获取站点数据开始");
		LambdaQueryWrapper<CrawlSite> q = new LambdaQueryWrapper<>();
		q.eq(CrawlSite::getStatus, "0").last("limit 0 , 10");
		List<CrawlSite> list = siteMapper.selectList(q);
		while (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				CrawlSite crawlSite = new CrawlSite();
				crawlSite = list.get(i);
				String siteName = crawlSite.getSite() + "_" + crawlSite.getChannel();
				try {
					String baseUrl = list.get(i).getBaseUrl();
					crawlSite.setInitUrl(baseUrl + list.get(i).getInitUrl());
					crawlSite.setPageUrl(baseUrl + list.get(i).getPageUrl());

					// 有些网站详情页面不在当前根目录下
					if (!list.get(i).getMatchUrl().contains("//www.")) {
						crawlSite.setMatchUrl(baseUrl + list.get(i).getMatchUrl());
					} else {
						crawlSite.setMatchUrl(list.get(i).getMatchUrl());
					}

					CommonCrawler crawler = new CommonCrawler("crawl", true, crawlSite, this.getBaseMapper(),
							crawlMonth);
					// CommonCrawler2 crawler = new CommonCrawler2("crawl", true, crawlSite,
					// this.getBaseMapper());
					crawler.start(3);
					/* 开始阻塞 */
					while (crawler.isResumable()) {
					}
					/* 结束后输出 */
					log.info("获取站点数据结束" + siteName);
					CrawlSite site = new CrawlSite();
					site.setId(list.get(i).getId());
					site.setStatus(1);
					siteMapper.updateById(site);
				} catch (Exception e) {
					log.info("获取站点数据 error " + siteName, e);
				}
			}
			list = siteMapper.selectList(q);
		}

	}

	public void initData(int siteId) {

		LambdaQueryWrapper<CrawlData> q0 = new LambdaQueryWrapper<>();
		q0.gt(CrawlData::getId, 0);
		dataMapper.delete(q0);

		CrawlSite site = new CrawlSite();
		site.setStatus(1);
		LambdaQueryWrapper<CrawlSite> q = new LambdaQueryWrapper<>();
		q.gt(CrawlSite::getId, 0);
		siteMapper.update(site, q);

		site.setStatus(0);
		if (siteId == 0) {
			q.gt(CrawlSite::getId, siteId);
		} else {
			q.eq(CrawlSite::getId, siteId);
		}
		siteMapper.update(site, q);
	}

	public List<String> getCrawlMonth(int beforeMonth) {
		List<String> crawlDate = new ArrayList<String>();
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		DateFormat format = new SimpleDateFormat("yyyyMM");
		for (int i = 0; i < beforeMonth + 1; i++) {
			date = calendar.getTime();
			calendar.setTime(date);
			String d = format.format(date);
			crawlDate.add(d);
			calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
		}
		return crawlDate;
	}

	public List<String> getCrawlDate(int beforeDay) {
		List<String> crawlDate = new ArrayList<String>();
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < beforeDay + 1; i++) {
			date = calendar.getTime();
			calendar.setTime(date);
			String d = format.format(date);
			crawlDate.add(d);
			calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
		}
		return crawlDate;
	}

	public List<String> getLastDate() {
		List<String> crawlDate = new ArrayList<String>();
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - 1);
		date = calendar.getTime();
		calendar.setTime(date);
		String d = format.format(date);
		crawlDate.add(d);
		crawlDate.add(d);
		return crawlDate;
	}

	@Override
//	@Scheduled(cron = "0/5 * * * * *")
	public synchronized void beijing() throws Exception {
		log.info("test");
		// list https://ggzyfw.beijing.gov.cn/jyxxggjtbyqs/index.html
		// detail https://ggzyfw.beijing.gov.cn/jyxxggjtbyqs/20201112/1245169.html
		WebData webData = new WebData();
		String baseUrl = "https://ggzyfw.beijing.gov.cn/jyxxggjtbyqs/";
		webData.setInitUrl(baseUrl + "index.html");
		webData.setPageUrl(baseUrl + "index_");
		webData.setPageStart(2);
		webData.setPageEnd(4);
		webData.setRegex(baseUrl + ".*");
		webData.setMatchUrl(baseUrl + ".*/.*");
		webData.setContent("div.div-content");
		webData.setTitle("meta[http-equiv=ArticleTitle]");
		webData.setKeyword("meta[http-equiv=ColumnKeywords]");
		webData.setPublishTime("meta[http-equiv=PubDate]");
		webData.setSite("meta[http-equiv=SiteName]");
		webData.setDomain("meta[http-equiv=SiteDomain]");
		// CommonCrawler crawler = new CommonCrawler("crawl", true,
		// webData,this.getBaseMapper());
		// crawler.start(3);

	}

}
