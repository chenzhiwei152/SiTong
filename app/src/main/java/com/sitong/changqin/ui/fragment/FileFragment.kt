package com.sitong.changqin.ui.fragment

import android.view.View
import com.jyall.bbzf.base.BaseFragment
import com.jyall.bbzf.base.BasePresenter
import com.jyall.bbzf.base.IBaseView
import com.jyall.bbzf.extension.jump
import com.sevenstringedzithers.sitong.R
import com.sitong.changqin.ui.activity.CollectionActivity
import com.sitong.changqin.ui.activity.LocalDownloadActivity
import kotlinx.android.synthetic.main.fragment_file.*

/**
 * create by chen.zhiwei on 2018-8-15
 */
class FileFragment : BaseFragment<IBaseView, BasePresenter<IBaseView>>(), IBaseView, View.OnClickListener {
    override fun onClick(v: View?) {
        when(v?.id){
            R.id.rl_record->{

            }
            R.id.rl_collection->{
                activity?.jump<CollectionActivity>()
            }
            R.id.rl_download->{
                activity?.jump<LocalDownloadActivity>()
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_file

    override fun lazyLoad() {
    }

    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()
    override fun getRootView(): IBaseView = this

    override fun initViewsAndEvents() {
        rl_download.setOnClickListener(this)
        rl_collection.setOnClickListener(this)
        rl_record.setOnClickListener(this)
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null
    companion object {
        fun newInstance(): FileFragment {
            return FileFragment()
        }
    }
}