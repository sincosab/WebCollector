package cn.edu.hfut.dmic.service;

import com.baomidou.mybatisplus.extension.service.IService;

import cn.edu.hfut.dmic.entity.CrawlData;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 
 * @since 2021-01-05
 */
public interface ICrawlDataService extends IService<CrawlData> {

	void beijing() throws Exception;

}
