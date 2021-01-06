package cn.edu.hfut.dmic.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

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
//@Component
//@Lazy(false)

public class CrawlDataServiceImpl extends ServiceImpl<CrawlDataMapper, CrawlData> implements ICrawlDataService {
	@Resource
	private CrawlSiteMapper siteMapper;

	@Override
	// @Scheduled(cron = "0/5 * * * * *")
	@Scheduled(cron = "0 0 */8 * * ?")

	@PostConstruct
	public synchronized void getSite() throws Exception {
		log.info("getSite begin");
		LambdaQueryWrapper<CrawlSite> q = new LambdaQueryWrapper<>();
		q.eq(CrawlSite::getStatus, "1").last("limit 0 , 10");
		List<CrawlSite> list = siteMapper.selectList(q);
		while (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				CrawlSite crawlSite = new CrawlSite();
				crawlSite = list.get(i);
				try {
					String baseUrl = list.get(i).getBaseUrl();
					crawlSite.setInitUrl(baseUrl + list.get(i).getInitUrl());
					crawlSite.setPageUrl(baseUrl + list.get(i).getPageUrl());
					crawlSite.setRegex(baseUrl + list.get(i).getRegex());
					crawlSite.setMatchUrl(baseUrl + list.get(i).getMatchUrl());
					CommonCrawler crawler = new CommonCrawler("crawl", true, crawlSite, this.getBaseMapper());
					crawler.start(3);
					/* 开始阻塞 */
					while (crawler.isResumable()) {
					}
					/* 结束后输出 */
					log.info("getSite end" + crawlSite.getDomainName());
					CrawlSite site = new CrawlSite();
					site.setId(list.get(i).getId());
					site.setStatus(2);
					siteMapper.updateById(site);
				} catch (Exception e) {
					log.info("getSite error " + crawlSite.getDomainName(), e);
				}
			}
			list = siteMapper.selectList(q);
		}

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
