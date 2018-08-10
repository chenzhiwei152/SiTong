package com.jyall.bbzf.base


interface IBaseView {

    fun showLoading(isCancleAble:Boolean=true)

    fun dismissLoading()

    fun showCommonView()

    fun showNoNetView()

    fun showErrorView()

    fun showEmptyView()
}
