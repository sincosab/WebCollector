/*
 * Copyright (C) 2015 hu
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package cn.edu.hfut.dmic.webcollector.example;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;

import cn.edu.hfut.dmic.contentextractor.WebData;
import cn.edu.hfut.dmic.entity.CrawlData;
import cn.edu.hfut.dmic.entity.CrawlSite;
import cn.edu.hfut.dmic.mapper.CrawlDataMapper;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.rocks.BreadthCrawler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonCrawler extends BreadthCrawler {
	private CrawlSite site;
	private String matchUrls = "";
	@Resource
	private CrawlDataMapper mapper;

	public CommonCrawler(String crawlPath, boolean autoParse, WebData webData) {
		super(crawlPath, autoParse);

		addSeed(webData.getInitUrl());
		for (int i = webData.getPageStart(); i < webData.getPageEnd() + 1; i++) {
			addSeed(webData.getPageUrl() + i + ".htm");
		}
		addRegex(webData.getRegex());
		addRegex("-.*#.*");
		// 需要抓取图片时设置为true，并加入图片的正则规则
		// setParseImg(true);
		matchUrls = webData.getMatchUrl();
		// 设置每个线程的抓取间隔（毫秒）
//        setExecuteInterval(1000);
		getConf().setExecuteInterval(1000);
		// 设置线程数
		// setThreads(30);
	}

	public CommonCrawler(String crawlPath, boolean autoParse, CrawlSite crawlSite, CrawlDataMapper baseMapper) {
		super(crawlPath, autoParse);
		mapper = baseMapper;
		addSeed(crawlSite.getInitUrl());
		for (int i = crawlSite.getPageStart(); i < crawlSite.getPageEnd() + 1; i++) {
			addSeed(crawlSite.getPageUrl() + i + ".htm");
		}
		addRegex(crawlSite.getRegex());
		addRegex("-.*#.*");
		matchUrls = crawlSite.getMatchUrl();
		site = crawlSite;
//        setExecuteInterval(1000);
		getConf().setExecuteInterval(1000);
		// 设置线程数
		// setThreads(30);
	}

	@Override
	public void visit(Page page, CrawlDatums next) {
		if (page.matchUrl(matchUrls)) {
			String content = getContent(page, site.getContent());
			String title=getTitle(page, site.getTitle());
			String siteName = getMeta(page, site.getSite());
			String keyword = getMeta(page, site.getKeyword());
			String url = page.crawlDatum().url();
			String publishTime = getMeta(page, site.getPublishTime());
			String siteDomain = getMeta(page, site.getDomain());
			CrawlData crawlData = new CrawlData();
			crawlData.setTitle(title);
			crawlData.setContent(content);
			crawlData.setKeyword(keyword);
			crawlData.setDomain(siteDomain);
			crawlData.setSite(siteName);
			crawlData.setUrl(url);
			crawlData.setPublishTime(publishTime);
			log.info(JSON.toJSONString(crawlData));
			mapper.insert(crawlData);
		}
	}
	public String getCommon(Page page, String features) {
		String str = "";
		// 多个特征用，分割
		try {
			String featuresArray[] = features.split(",");
			for (int i = 0; i < featuresArray.length; i++) {
				str = page.select(features).first().text();
				if (StringUtils.isNotBlank(str)) {
					break;
				}
			}
		} catch (Exception e) {
			log.error("getCommon 解析内容异常:" + e.getLocalizedMessage(),e);
		}
		return str;
	}
	
	public String getContent(Page page, String features) {
		String str = "";
		// 多个特征用，分割
		try {
			String featuresArray[] = features.split(",");
			for (int i = 0; i < featuresArray.length; i++) {
				str = page.select(features).first().text();
				if (StringUtils.isNotBlank(str)) {
					break;
				}
			}
		} catch (Exception e) {
			log.error("getContent 解析内容异常:" + e.getLocalizedMessage(),e);
		}
		return str;
	}
	public String getTitle(Page page, String features) {
		String title="";
		if (site.getTitle().contains("meta[")) {
			title = getMeta(page, site.getTitle());
		} else {
			title = getCommon(page, site.getTitle());
		}
		return title;
	}
	
	public String getMeta(Page page, String features) {
		String str = "";
		try {
			Elements attr = page.select(features);
			if (attr != null) {
				Element element = attr.get(0);
				return element.attr("content");
			}
		} catch (Exception e) {
			log.error("getMeta 解析内容异常:" + e.getLocalizedMessage(),e);
		}
		return str;
	}
	
}
