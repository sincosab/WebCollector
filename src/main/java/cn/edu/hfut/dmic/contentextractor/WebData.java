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
package cn.edu.hfut.dmic.contentextractor;

import org.jsoup.nodes.Element;

import lombok.Data;

/**
 *
 * @author hu
 */
@Data
public class WebData {
    private String initUrl = null;
    private int pageStart = 0;
    private int pageEnd = 0;
    private String pageUrl = null;
    private String matchUrl = null;
    private String regex = null;
    private String title = null;
    private String keyword = null;
    private String author = null;
    private String content = null;
    private String source = null;
    private String url = null;
    private String publishTime  = null;
    private String  siteName  = null;
    private String  siteDomain  = null;
	

   
}
