package cn.edu.hfut.dmic.service.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import cn.edu.hfut.dmic.contentextractor.WebData;
import cn.edu.hfut.dmic.entity.CrawlData;
import cn.edu.hfut.dmic.entity.CrawlSite;
import cn.edu.hfut.dmic.mapper.CrawlDataMapper;
import cn.edu.hfut.dmic.mapper.CrawlSiteMapper;
import cn.edu.hfut.dmic.service.ICrawlDataService;
import cn.edu.hfut.dmic.service.IGgzyService;
import cn.edu.hfut.dmic.webcollector.example.CommonCrawler;
import cn.edu.hfut.dmic.webcollector.example.CommonCrawler2;
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
//@Component
//@Lazy(false)

public class CrawlDataServiceImpl extends ServiceImpl<CrawlDataMapper, CrawlData> implements ICrawlDataService {
	@Resource
	private CrawlSiteMapper siteMapper;

	@Resource
	private CrawlDataMapper dataMapper;

	//@Autowired
//private IGgzyService  ggzyService;
	
	//@Scheduled(cron = "0 0 */8 * * ?")
	//@PostConstruct
	public synchronized void getSite() throws Exception {
		log.info("初始化数据开始");
		int siteId = 0;
		int beforeMonth = 1;
		initData(siteId);
		List<String> crawlDate = getCrawlDate(beforeMonth);
		executeSite(crawlDate);
	}

	public synchronized void executeSite(List<String> crawlDate) throws Exception {
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
							crawlDate);
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

	public List<String> getCrawlDate(int beforeMonth) {
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

	@PostConstruct
	public void getGgzySite() throws Exception {
	//	ggzyService.getSite(dataMapper);
	///	GgzyServiceImpl.main(null);
		GgzyServiceImpl s= new GgzyServiceImpl("ggzy",true,dataMapper);
		s.start(2);
	
	}
}
