package cn.edu.hfut.dmic.entity;

import java.io.Serializable;
import java.util.List;

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
public class GgzyResult implements Serializable {

	private static final long serialVersionUID = 1L;

	int ttlpage;
	int ttlrow;
	int usetime;
	int currentpage;
	boolean success;
	List<GgzyData> data;
}
