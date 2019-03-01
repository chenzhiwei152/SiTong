package com.sevenstringedzithers.sitong.ui.activity

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import com.github.huajianjiang.expandablerecyclerview.widget.ExpandableAdapter
import com.jyall.android.common.utils.SharedPrefUtil
import com.jyall.bbzf.extension.jump
import com.jyall.bbzf.extension.toast
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.base.BaseActivity
import com.sevenstringedzithers.sitong.base.Constants
import com.sevenstringedzithers.sitong.mvp.contract.IndexContract
import com.sevenstringedzithers.sitong.mvp.model.bean.MemberMusciChild
import com.sevenstringedzithers.sitong.mvp.model.bean.MemberMusciParent
import com.sevenstringedzithers.sitong.mvp.model.bean.MusicBean
import com.sevenstringedzithers.sitong.mvp.persenter.IndexPresenter
import com.sevenstringedzithers.sitong.ui.adapter.MemberMusicListAdapter
import com.sevenstringedzithers.sitong.utils.AppUtil
import kotlinx.android.synthetic.main.activity_music_member.*
import kotlinx.android.synthetic.main.layout_common_title.*

class MusicMemberListActivity : BaseActivity<IndexContract.View, IndexPresenter>(), IndexContract.View {
    override fun getPresenter(): IndexPresenter = IndexPresenter()
    private var mAdapter: MemberMusicListAdapter? = null
    private var mData: java.util.ArrayList<MemberMusciParent>? = arrayListOf()
    override fun getRootView(): IndexContract.View = this

    override fun toast_msg(msg: String) {
        toast(msg)
    }

    override fun getDataSuccess(musicList: ArrayList<MusicBean>) {
        for (musicBean in musicList){
            var bean=MemberMusciParent()
            bean.levelName=musicBean.levelName
            bean.type= AppUtil.TYPE_PARENT[1]
            var list= arrayListOf<MemberMusciChild>()
            if ( musicBean.type==1){
                for (music in musicBean.musics){
                        var bean= MemberMusciChild()
                        bean.id= music.id.toString()
                        bean.name=music.name
                        bean.type= AppUtil.TYPE_CHILD[1]
                        list.add(bean)
                }
            }else if (musicBean.type==2){
                for (musics in musicBean.musics ){
                    for (music in musics.musics){
                        var bean= MemberMusciChild()
                        bean.id= music.id.toString()
                        bean.name=music.name
                        bean.type= AppUtil.TYPE_CHILD[1]
                        list.add(bean)
                    }
                }
            }
            bean.childList=list
            mData?.add(bean)
        }
        mAdapter?.updateList(mData)

    }

    override fun getLayoutId(): Int = R.layout.activity_music_member

    override fun initViewsAndEvents() {
        initTitle()

//        mData = AppUtil.getListData(0)
        mAdapter = MemberMusicListAdapter(this, mData)
        mAdapter?.setExpandCollapseMode(ExpandableAdapter.ExpandCollapseMode.MODE_DEFAULT)

        rv_list.setAdapter(mAdapter)

       var  musicList = SharedPrefUtil.getObj(this, Constants.musicList) as ArrayList<MusicBean>?
        if (musicList==null){
            mPresenter?.getMusicList("1")
        }else{
            getDataSuccess(musicList)
        }
        mAdapter?.childClickTargets(R.id.child)?.listenChildClick(object : ExpandableAdapter.OnChildClickListener {
            override fun onChildClick(parent: RecyclerView?, view: View?) {
                var childAdapterPos = parent?.getChildAdapterPosition(parent.findContainingItemView(view))
//                var ss=mAdapter!!.getParentPosition(childAdapterPos!!)
                var layoutPosition=mAdapter?.getChildPosition(childAdapterPos!!)
//                toast_msg("mPosition:"+ss+"------------"+childAdapterPos+"----layoutPosition:"+layoutPosition)
                var bund=Bundle()
                bund.putString("id",mData?.get(mAdapter!!.getParentPosition(childAdapterPos!!))?.childList?.get(layoutPosition!!)?.id)
                jump<MemberListActivity>(dataBundle = bund)
            }

        })
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null
    private fun initTitle() {
        iv_back.setOnClickListener { finish() }
        tv_title.setText("会员")
        iv_menu.visibility = View.GONE
    }
}