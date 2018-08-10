package com.jyall.bbzf.base

import java.io.Serializable

/**
 * Created by du.yongliang on 2018/7/16.
 */
class Page(var beginPageIndex: Int, var endPageIndex: Int, var pageCount: Int, var pageNum: Int, var pageSize: Int,var recordList: List<Any> , var totalCount: Int): Serializable{

}