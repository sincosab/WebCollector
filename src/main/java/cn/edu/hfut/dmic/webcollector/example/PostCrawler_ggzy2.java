/*
 * Copyright (C) 2014 hu
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

import cn.edu.hfut.dmic.webcollector.model.CrawlDatum;
import cn.edu.hfut.dmic.webcollector.model.CrawlDatums;
import cn.edu.hfut.dmic.webcollector.model.Page;
import cn.edu.hfut.dmic.webcollector.plugin.net.OkHttpRequester;
import cn.edu.hfut.dmic.webcollector.plugin.rocks.BreadthCrawler;
import cn.edu.hfut.dmic.webcollector.util.ExceptionUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.google.gson.JsonObject;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * 本教程演示了如何自定义http请求
 *
 * 有些爬取任务中，可能只有部分URL需要使用POST请求，我们可以利用2.20版本中添 加的MetaData功能，来完成POST请求的定制。
 *
 * 使用MetaData除了可以标记URL是否需要使用POST，还可以存储POST所需的参数信息
 *
 * 教程中还演示了如何定制Cookie、User-Agent等http请求头信息
 *
 * WebCollector中已经包含了org.json的jar包
 * http://deal.ggzy.gov.cn/ds/deal/dealList.jsp
 * @author hu
 */
public class PostCrawler_ggzy2 extends BreadthCrawler {

    /**
     * 
     * 假设我们要爬取三个链接 1)http://www.A.com/index.php 需要POST，并需要POST表单数据username:John
     * 2)http://www.B.com/index.php?age=10 需要POST，数据直接在URL中 ，不需要附带数据 3)http://www.C.com/
     * 需要GET
     */
	

	 
	public static Headers SetHeaders(Map<String, String> headersParams) {
		Headers headers = null;
		okhttp3.Headers.Builder headersbuilder = new okhttp3.Headers.Builder();
		if (!headersParams.isEmpty()) {
			Iterator<String> iterator = headersParams.keySet().iterator();
			String key = "";
			while (iterator.hasNext()) {
				key = iterator.next().toString();
				headersbuilder.add(key, headersParams.get(key));
			}
		}
		headers = headersbuilder.build();
		return headers;
		}

	//TIMEBEGIN_SHOW=2021-01-27&TIMEEND_SHOW=2021-02-05&TIMEBEGIN=2021-01-27&TIMEEND=2021-02-05&SOURCE_TYPE=2&DEAL_TIME=01&DEAL_CLASSIFY=01&DEAL_STAGE=0100&DEAL_PROVINCE=370000&DEAL_CITY=371500&DEAL_PLATFORM=6738e78793af4262be30a668ff8c1e2c&BID_PLATFORM=0&DEAL_TRADE=0&isShowAll=1&PAGENUMBER=4&FINDTXT=

    public PostCrawler_ggzy2(final String crawlPath, boolean autoParse) {
        super(crawlPath, autoParse);

        addSeed(new CrawlDatum("http://deal.ggzy.gov.cn/ds/deal/dealList_find.jsp?TIMEBEGIN_SHOW=2021-01-28&TIMEEND_SHOW=2021-02-06&TIMEBEGIN=2021-01-28&TIMEEND=2021-02-06&SOURCE_TYPE=1&DEAL_TIME=02&DEAL_CLASSIFY=00&DEAL_STAGE=0000&DEAL_PROVINCE=0&DEAL_CITY=0&DEAL_PLATFORM=0&BID_PLATFORM=0&DEAL_TRADE=0&isShowAll=1&PAGENUMBER=1&FINDTXT=")
                .meta("method", "POST"));
           //     .meta("username", "John"));
     

        setRequester(new OkHttpRequester(){
            @Override
            public Request.Builder createRequestBuilder(CrawlDatum crawlDatum) {
                Request.Builder requestBuilder = super.createRequestBuilder(crawlDatum);
         
              //  requestBuilder.addHeader("Content-Type", "text/html;charset=UTF-8"); 
                requestBuilder.addHeader("Content-Type", "application/x-www-form-urlencoded"); 
                requestBuilder.addHeader("Accept", "application/json, text/javascript, */*; q=0.01"); 
                requestBuilder.addHeader("Cookie", "JSESSIONID=6df916b086bc959c0b7420f783fa; JSESSIONID=6df916b086bc959c0b7420f783fa; insert_cookie=97324480");  
                requestBuilder.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/88.0.4324.104 Safari/537.36");  
                requestBuilder.addHeader("Host", "deal.ggzy.gov.cn"); 
                requestBuilder.addHeader("Origin", "http://deal.ggzy.gov.cn");  
                requestBuilder.addHeader("Referer", "http://deal.ggzy.gov.cn/ds/deal/dealList.jsp");  

              
                String method = crawlDatum.meta("method");
                if(method.equals("POST")){
                    RequestBody requestBody;
                    String username = crawlDatum.meta("username");
                    // 如果没有表单数据username，POST的数据直接在URL中
                    MediaType mediaType= MediaType.parse("raw");
                        // 根据meta构建POST表单数据
               
                  //  headMap.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");  

                   // RequestBody requestBody1= new MultipartBody.Builder();
                    
            	//	Headers setHeaders = SetHeaders(headMap);

                        requestBody = new MultipartBody.Builder()
             //  .setType(MediaType.parse("multipart/raw"))
              	     .setType(MultipartBody.FORM)
            //      .addPart(setHeaders , null)
           // .setType(mediaType)
               
   
              //    .addFormDataPart("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                                .addFormDataPart("TIMEBEGIN_SHOW","2021-01-28")
                                .addFormDataPart("TIMEEND_SHOW", "2021-02-06")
                                .addFormDataPart("TIMEBEGIN","2021-01-28")
                                .addFormDataPart("TIMEEND", "2021-02-06")
                                .addFormDataPart("SOURCE_TYPE","1")
                                .addFormDataPart("DEAL_TIME","02")
                                .addFormDataPart("DEAL_CLASSIFY", "02")
                                .addFormDataPart("DEAL_STAGE", "0200")
                                .addFormDataPart("DEAL_PROVINCE","0")
                                .addFormDataPart("DEAL_CITY", "0")
                                .addFormDataPart("DEAL_PLATFORM","0")
                                .addFormDataPart("BID_PLATFORM","0")
                                .addFormDataPart("DEAL_TRADE", "0")
                                .addFormDataPart("isShowAll","1")
                                .addFormDataPart("PAGENUMBER", "1")
                                .addFormDataPart("FINDTXT", "")
                          
                                .build();
                  
                    return requestBuilder.post(requestBody);
                }

                //执行这句会抛出异常
                ExceptionUtils.fail("wrong method: " + method);
                return null;
            }
        });


    }



//    @Override
//    public Page getResponse(CrawlDatum crawlDatum) throws Exception {
//        HttpRequest request = new HttpRequest(crawlDatum.url());
//
//        request.setMethod(crawlDatum.meta("method"));
//        String outputData = crawlDatum.meta("outputData");
//        if (outputData != null) {
//            request.setOutputData(outputData.getBytes("utf-8"));
//        }
//        return request.responsePage();
//        /*
//        //通过下面方式可以设置Cookie、User-Agent等http请求头信息
//        request.setCookie("xxxxxxxxxxxxxx");
//        request.setUserAgent("WebCollector");
//        request.addHeader("xxx", "xxxxxxxxx");
//         */
//    }

    @Override
    public void visit(Page page, CrawlDatums next) {
        JsonObject jsonObject = page.jsonObject();
        System.out.println("JSON信息：" + jsonObject);
    }

    /**
     *
     * @param args 参数
     * @throws Exception 异常
     */
    public static void main(String[] args) throws Exception {
		
		  System.setProperty("http.proxyHost", "localhost");
		  System.setProperty("http.proxyPort", "8888");
		  System.setProperty("https.proxyHost", "localhost");
		  System.setProperty("https.proxyPort", "8888");
		 
        PostCrawler_ggzy2 crawler = new PostCrawler_ggzy2("json_crawler", true);
        crawler.start(1);
    }

}
