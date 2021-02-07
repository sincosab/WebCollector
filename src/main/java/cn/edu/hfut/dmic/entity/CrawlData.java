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
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("crawl_data")
public class CrawlData implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private Long siteId;
    private String siteName;
    private String channel;
    private String type;
    private String province;
    private String city;

    private String title;
    private String content;
    private String publish;
    private Integer status;
    private String url;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;


}
