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
@TableName("crawl_site")
public class CrawlSite implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String site;

    private String initUrl;

    private String pageUrl;

    private Integer pageStart;

    private Integer pageEnd;

    private String regex;

    private String matchUrl;

    private String metaFlag;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
