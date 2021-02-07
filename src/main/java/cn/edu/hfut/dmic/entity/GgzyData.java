package cn.edu.hfut.dmic.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
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
	String classifyShow;
	String tradeShow;
	String districtShow;
	String url;
	String stageShow;
	String titleShow;

}
