package cn.edu.hfut.dmic.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author
 * @since 2021-01-05
 */

@EqualsAndHashCode(callSuper = false)
@Data
public class GgzyData implements Serializable {

	private static final long serialVersionUID = 1L;
	String classify;
	String title;
	String timeShow;

	String stageName;
	String platformName;
	//业务类型
	String classifyShow;
	//行业
	String tradeShow;
	//行政区
	String districtShow;
	String url;
	//信息类型，招标，中标
	String stageShow;
	String titleShow;

}
