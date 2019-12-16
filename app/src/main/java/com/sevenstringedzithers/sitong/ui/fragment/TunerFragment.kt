package com.sevenstringedzithers.sitong.ui.fragment

import android.view.View
import be.tarsos.dsp.AudioDispatcher
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor
import com.jyall.android.common.utils.LogUtils
import com.jyall.bbzf.extension.toast
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.base.BaseFragment
import com.sevenstringedzithers.sitong.base.BasePresenter
import com.sevenstringedzithers.sitong.base.IBaseView
import com.sevenstringedzithers.sitong.view.pickview.ScrollPickerView
import kotlinx.android.synthetic.main.fragment_tuner.*
import java.text.DecimalFormat

/**
 * 调音器
 * create by chen.zhiwei on 2018-8-15
 */
class TunerFragment : BaseFragment<IBaseView, BasePresenter<IBaseView>>(), IBaseView {
    internal var mThread: Thread? = null
    override fun getLayoutId(): Int = R.layout.fragment_tuner
    private var list_1 = arrayListOf<String>("正调","紧五弦", "紧二五弦")
    private var list_2 = arrayListOf<String>("1", "2", "3", "4", "5", "6", "7")
    override fun lazyLoad() {
    }

    var p: PitchProcessor? = null
    var dispatcher: AudioDispatcher? = null
    private var list: ArrayList<ArrayList<Float>>? = null
    private var listRange: ArrayList<ArrayList<Float>>? = null
    private var currentValue: Float = 0.toFloat()
    private var minValue: Float = 0.toFloat()
    private var maxValue: Float = 0.toFloat()
    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()
    override fun getRootView(): IBaseView = this
    var decimalFormat = DecimalFormat(".000")//构造方法的字符格式这里如果小数不足2位,会以0补足.
    override fun initViewsAndEvents() {

        picker_01.data = list_1 as List<CharSequence>?
        picker_02.data = list_2 as List<CharSequence>?


        var postion1: Int = 0
        var postion2: Int = 0
        list = arrayListOf()
        listRange = arrayListOf()
        var list3 = arrayListOf<Float>(65.406f, 73.416f, 87.307f, 97.999f, 116.54f, 130.813f, 146.832f)
        var list4 = arrayListOf<Float>(65.406f, 77.78f, 87.307f, 97.999f, 116.54f, 130.813f, 146.832f)
        var list2 = arrayListOf<Float>(65.406f, 77.78f, 87.307f, 97.999f, 110.000f, 130.813f, 146.832f)

        list?.add(list2)
        list?.add(list3)
        list?.add(list4)
        var list1 = arrayListOf<Float>(0.389f, 0.436f, 0.519f, 0.583f, 0.654f, 0.778f, 0.873f)
        var list6 = arrayListOf<Float>(0.389f, 0.436f, 0.519f, 0.583f, 0.693f, 0.778f, 0.873f)
        var list5 = arrayListOf<Float>(0.389f, 0.463f, 0.519f, 0.583f, 0.693f, 0.778f, 0.873f)
        listRange?.add(list5)
        listRange?.add(list1)
        listRange?.add(list6)
        picker_01.setOnSelectedListener(object : ScrollPickerView.OnSelectedListener {
            override fun onSelected(scrollPickerView: ScrollPickerView<*>?, position: Int) {
                postion1 = position
                setValue(postion1, postion2)
            }

        })
        var num2 = 1
        picker_02.setOnSelectedListener(object : ScrollPickerView.OnSelectedListener {
            override fun onSelected(scrollPickerView: ScrollPickerView<*>?, position: Int) {
                postion2 = position
                setValue(postion1, postion2)
            }

        })
        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 512)

        val pdh = PitchDetectionHandler { result, e ->
            val pitchInHz = result.pitch
            try {
                if (pitchInHz < 0) {
                    ver_line.setCurrentValue(0F)
                } else {
                    ver_line.setCurrentValue(decimalFormat.format(pitchInHz).toFloat())
                }

            } catch (ex: java.lang.Exception) {
            }

        }
        setValue(postion1, postion2)
        p = PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050f, 1024, pdh)
        dispatcher?.addAudioProcessor(p)
        try {
            mThread = Thread(dispatcher, "Audio Dispatcher")
            mThread?.start()
        } catch (e: java.lang.Exception) {
        }
    }

    private fun setValue(p1: Int, p2: Int) {
        ver_line.setValue(listRange?.get(p1)!!.get(p2), "" + list?.get(p1)!!.get(p2))
    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null

    companion object {
        fun newInstance(): TunerFragment {
            return TunerFragment()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            dispatcher?.stop()
            p?.processingFinished()
            mThread?.interrupt()
        } catch (e: Exception) {
        }

    }
}