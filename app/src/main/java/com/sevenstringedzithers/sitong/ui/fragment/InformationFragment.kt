package com.sevenstringedzithers.sitong.ui.fragment

import android.view.View
import com.jyall.bbzf.base.*
import com.jyall.bbzf.extension.jump
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.base.Constants.Tag.UPDATE_USER_INFO
import com.sevenstringedzithers.sitong.mvp.model.bean.UserInfo
import com.sevenstringedzithers.sitong.ui.activity.*
import kotlinx.android.synthetic.main.fragment_information.*

/**
 * create by chen.zhiwei on 2018-8-15
 */
class InformationFragment : BaseFragment<IBaseView, BasePresenter<IBaseView>>(), IBaseView, View.OnClickListener {
    private var user: UserInfo? = null
    private var ac:MineActivity?=null
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.rl_name -> {
                EditActivity.newInstance(requireActivity(),"1","编辑姓名","姓名")
            }
            R.id.rl_belong -> {
                activity?.jump<BelongActivity>()
            }
            R.id.rl_email -> {
                EditActivity.newInstance(requireActivity(),"1","编辑邮箱","邮箱")
            }
            R.id.rl_phone -> {
                EditActivity.newInstance(requireActivity(),"1","编辑电话","电话")
            }
            R.id.rl_address -> {
                EditActivity.newInstance(requireActivity(),"1","编辑地址","地址")
            }
            R.id.rl_exp_record -> {
                activity?.jump<ExperienceRecordActivity>()
            }
            R.id.rl_question -> {
                activity?.jump<QuestionsActivity>()
            }
            R.id.rl_feedback -> {
                activity?.jump<FeedbackActivity>()
            }
            R.id.tv_quit -> {
                BaseContext.instance.logout()
            }
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_information

    override fun lazyLoad() {
    }

    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()
    override fun getRootView(): IBaseView = this

    override fun initViewsAndEvents() {
        rl_name.setOnClickListener(this)
        rl_belong.setOnClickListener(this)
        rl_email.setOnClickListener(this)
        rl_phone.setOnClickListener(this)
        rl_address.setOnClickListener(this)
        rl_exp_record.setOnClickListener(this)
        rl_question.setOnClickListener(this)
        rl_feedback.setOnClickListener(this)
        tv_quit.setOnClickListener(this)
    }

    override fun isRegistEventBus(): Boolean = true

    override fun onMessageEvent(eventBusCenter: EventBusCenter<Object>) {
        super.onMessageEvent(eventBusCenter)
        if (eventBusCenter!=null){
            if (eventBusCenter.evenCode==UPDATE_USER_INFO){
                user = eventBusCenter.data as UserInfo
                tv_name_content.text = user?.nickname
                tv_belong_content.text = user?.carillon
                tv_email_content.text = user?.email
                tv_phone_content.text = user?.phone
                tv_address_content.text = user?.address
            }
        }
    }
    override fun isNeedLec(): View? = null

    companion object {
        fun newInstance(): InformationFragment {
            return InformationFragment()
        }
    }
}