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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class CommonCrawler2 extends BreadthCrawler {
	private CrawlSite site;
	private String matchUrls = "";
	private CrawlDataMapper mapper;

	public CommonCrawler2(String crawlPath, boolean autoParse, CrawlSite crawlSite, CrawlDataMapper baseMapper) {
		super(crawlPath, autoParse);
		mapper = baseMapper;
		addSeed(crawlSite.getInitUrl());
		for (int i = crawlSite.getPageStart(); i < crawlSite.getPageEnd() + 1; i++) {
			addSeed(crawlSite.getPageUrl() + i + ".htm");
		}
		String url = crawlSite.getMatchUrl();
		addRegex(url);
		matchUrls =   url ;
		addRegex(url);
		site = crawlSite;
		getConf().setExecuteInterval(1000);
		// 设置线程数
		// setThreads(30);
	}


	@Override
	public void visit(Page page, CrawlDatums next) {
		boolean succeed = false;
		if (page.matchUrl(matchUrls)) {
		
			String title = getTitle(page, site.getTitle());

			String content = getContent(page, site.getContent());

			String publishTime = getPublishTime(page, site.getPublishTime());

			String siteName = getMeta(page, site.getSite());

			String keyword = getMeta(page, site.getKeyword());

			String url = page.crawlDatum().url();

			String siteDomain = getMeta(page, site.getDomain());
			CrawlData crawlData = new CrawlData();
			crawlData.setTitle(title);
			crawlData.setContent(content);
			crawlData.setPublishTime(publishTime);
			log.info(JSON.toJSONString(crawlData));
			boolean check = false;
		//	if (check) {
				mapper.insert(crawlData);
		//	}

		}
	}

	public boolean checkTitle(String title) {
		if (StringUtils.isNotBlank(title)) {
			return true;
		}
		return false;
	}

	public boolean checkContent(String content) {
		if (StringUtils.isNotBlank(content)) {
			return true;
		}
		return false;
	}

	public boolean checkPublishTime(String publishTime) {
		if (StringUtils.isBlank(publishTime)) {
			return false;
		}

		String str = "1988-05-20";
		String pattern = "\\d{4}(\\-|\\/|.)\\d{1,2}\\1\\d{1,2}";

		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(str);
		System.out.println(m.matches());

		return false;
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

	public String getContent(Page page, String features) {
		String str = "";
		// 多个特征用，分割
		try {
			String featuresArray[] = features.split(",");
			for (int i = 0; i < featuresArray.length; i++) {
				if (page.select(featuresArray[i]).first() != null) {
					str = page.select(featuresArray[i]).first().text();
					if (StringUtils.isNotBlank(str)) {
						break;
					}
				}
			}
		} catch (Exception e) {
			log.error("getContent 解析内容异常:" + e.getLocalizedMessage(), e);
		}
		return str;
	}

	public String getTitle(Page page, String features) {
		String title = "";
		if (site.getTitle().contains("meta[")) {
			title = getMeta(page, site.getTitle());
		} else {
			title = getCommon(page, site.getTitle());
		}
		return title;
	}

	public String getPublishTime(Page page, String features) {
		String title = "";
		if (features.contains("meta[")) {
			title = getMeta(page, features);
		} else {
			title = getCommon(page, features);
		}
		return title;
	}

	public String getMeta(Page page, String features) {
		String str = "";
		try {
			Elements attr = page.select(features);
			if (attr != null && attr.size() > 0) {
				Element element = attr.get(0);
				return element.attr("content");
			}
		} catch (Exception e) {
			log.error("getMeta 解析内容异常:" + e.getLocalizedMessage(), e);
		}
		return str;
	}

	public boolean matchUrl(String url) {

		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		DateFormat format = new SimpleDateFormat("yyyyMM");
		String d0 = format.format(date);
		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1); // 设置为上一个月
		date = calendar.getTime();
		String d1 = format.format(date);

		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
		date = calendar.getTime();
		String d2 = format.format(date);

		calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
		date = calendar.getTime();
		String d3 = format.format(date);
		String pattern0 = url + ".*" + d0 + "(0[0-9]|1[0-9]|2[0-9]).*";
		String pattern1 = url + ".*" + d1 + "(0[0-9]|1[0-9]|2[0-9]).*";
		String pattern2 = url + ".*" + d2 + "(0[0-9]|1[0-9]|2[0-9]).*";
		String pattern3 = url + ".*" + d3 + "(0[0-9]|1[0-9]|2[0-9]).*";

		Pattern r = Pattern.compile(pattern0);
		Matcher m = r.matcher(url);
		if (m.matches()) {
			return true;
		}

		r = Pattern.compile(pattern1);
		m = r.matcher(url);
		if (m.matches()) {
			return true;
		}

		r = Pattern.compile(pattern2);
		m = r.matcher(url);
		if (m.matches()) {
			return true;
		}

		r = Pattern.compile(pattern3);
		m = r.matcher(url);
		if (m.matches()) {
			return true;
		}
		return false;

	}
	public CommonCrawler2(String crawlPath, boolean autoParse, WebData webData) {
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
	

}
