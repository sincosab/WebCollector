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

import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.rocks.BreadthCrawler;



/**
 * WebCollector 2.x版本的tutorial(2.20以上) 
 * 2.x版本特性：
 * 1）自定义遍历策略，可完成更为复杂的遍历业务，例如分页、AJAX
 * 2）可以为每个URL设置附加信息(MetaData)，利用附加信息可以完成很多复杂业务，例如深度获取、锚文本获取、引用页面获取、POST参数传递、增量更新等。
 * 3）使用插件机制，WebCollector内置两套插件。
 * 4）内置一套基于内存的插件（RamCrawler)，不依赖文件系统或数据库，适合一次性爬取，例如实时爬取搜索引擎。
 * 5）内置一套基于Berkeley DB（BreadthCrawler)的插件：适合处理长期和大量级的任务，并具有断点爬取功能，不会因为宕机、关闭导致数据丢失。 
 * 6）集成selenium，可以对javascript生成信息进行抽取
 * 7）可轻松自定义http请求，并内置多代理随机切换功能。 可通过定义http请求实现模拟登录。 
 * 8）使用slf4j作为日志门面，可对接多种日志
 *
 * 可在cn.edu.hfut.dmic.webcollector.example包中找到例子(Demo)
 *
 * @author hu
 */
public class CrawlerGuangDong extends BreadthCrawler {

    /*
        该例子利用正则控制爬虫的遍历，
        另一种常用遍历方法可参考DemoTypeCrawler
    */
    int count=0;
    public CrawlerGuangDong(String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);
        String param="orgCode=&tradeTypeId=Construction&queryType=1&tradeItemId=gc_res_bulletin&bulletinName=&startTime=&endTime=&pageNum=";
        String url="http://bs.gdggzy.org.cn/osh-web/project/projectbulletin/bulletinList?";
        //addSeed("");
       // addSeed("http://www.ccgp.gov.cn/zcdt/index_"+i+".htm");
        //http://bs.gdggzy.org.cn/osh-web/project/projectbulletin/bulletinList?orgCode=&tradeTypeId=Construction&queryType=1&tradeItemId=gc_res_bulletin&bulletinName=&startTime=&endTime=&pageNum=1
        for (int i=1;i<2;i++) {
        addSeed(url+param+i);
        }
      // addSeed("http://www.ccgp.gov.cn/zcdt/index_2.htm");
     //   http://bs.gdggzy.org.cn/osh-web/project/projectbulletin/toBullDetail?bulletinId=3e31ddd26dd445fa8041e2ce75dd0e61&tradeTypeId=Construction&tradeItemId=gc_res_bulletin&queryType=1        
     
      //  http://bs.gdggzy.org.cn/osh-web/project/projectbulletin/toBullDetail?bulletinId=b95268e1225e4bf39299a10fffd290f5&tradeTypeId=Construction&tradeItemId=gc_res_bulletin&queryType=1 
        
        param="&tradeTypeId=Construction&tradeItemId=gc_res_bulletin&queryType=1";
	    
    	//url="/osh-web/project/projectbulletin/toBullDetail?bulletinId=";
        
     addRegex("http://bs.gdggzy.org.cn/osh-web/project/projectbulletin/.*");
        
       // addRegex(url+".*"+param);

      addRegex("-.*#.*");
        
        //需要抓取图片时设置为true，并加入图片的正则规则
//        setParseImg(true);
        
        //设置每个线程的抓取间隔（毫秒）
//        setExecuteInterval(1000);
        getConf().setExecuteInterval(1000);
        
        //设置线程数
       // setThreads(30);
    }

    /*
        可以往next中添加希望后续爬取的任务，任务可以是URL或者CrawlDatum
        爬虫不会重复爬取任务，从2.20版之后，爬虫根据CrawlDatum的key去重，而不是URL
        因此如果希望重复爬取某个URL，只要将CrawlDatum的key设置为一个历史中不存在的值即可
        例如增量爬取，可以使用 爬取时间+URL作为key。
    
        新版本中，可以直接通过 page.select(css选择器)方法来抽取网页中的信息，等价于
        page.getDoc().select(css选择器)方法，page.getDoc()获取到的是Jsoup中的
        Document对象，细节请参考Jsoup教程
    */
    @Override
    public void visit(Page page, CrawlDatums next) {
        String param="&tradeTypeId=Construction&tradeItemId=gc_res_bulletin&queryType=1";
    	    
    	String url="http://bs.gdggzy.org.cn/osh-web/project/projectbulletin/toBullDetail?bulletinId=.*";
    	
        param="&tradeTypeId=Construction&tradeItemId=gc_res_bulletin&queryType=1";
	    
      	//url="/osh-web/project/projectbulletin/toBullDetail?bulletinId=";
          
//url="http://bs.gdggzy.org.cn/osh-web/project/projectbulletin/toBullDetail?bulletinId=50a2d11df99d4b26991fe1c64030e821&tradeTypeId=Construction&tradeItemId=gc_res_bulletin&queryType=1"; 
url="http://bs.gdggzy.org.cn/osh-web/project/projectbulletin/toBullDetail?bulletinId=[0-9a-f]{32}&tradeTypeId=Construction&tradeItemId=gc_res_bulletin&queryType=1"; 
url="http://bs.gdggzy.org.cn/osh-web/project/projectbulletin/toBullDetail.*"; 

 //System.out.println("urlss:" +page.crawlDatum().url());
 //if (page.crawlDatum().url().contains(url)) {
  if (page.matchUrl(url)) {
           // String title = page.select("h1.title-article").first().text();
           // String author = page.select("a#uid").first().text();
   
           // System.out.println("title:" + title + "\tauthor:" + author);
            
            String content= page.select("div.tab-content-ds").first().text();
            System.out.println("content:" +content);
       }
    }


    public static void main(String[] args) throws Exception {
        CrawlerGuangDong crawler = new CrawlerGuangDong("crawl", true);
//        crawler.setResumable(true);
        crawler.start(2);
    }

}
