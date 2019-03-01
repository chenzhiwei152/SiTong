package com.sevenstringedzithers.sitong.ui.fragment

import android.view.View
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor
import com.sevenstringedzithers.sitong.R
import com.sevenstringedzithers.sitong.base.BaseFragment
import com.sevenstringedzithers.sitong.base.BasePresenter
import com.sevenstringedzithers.sitong.base.IBaseView
import com.sevenstringedzithers.sitong.view.pickview.ScrollPickerView
import kotlinx.android.synthetic.main.fragment_tuner.*

/**
 * 调音器
 * create by chen.zhiwei on 2018-8-15
 */
class TunerFragment : BaseFragment<IBaseView, BasePresenter<IBaseView>>(), IBaseView {
    internal var mThread: Thread? = null
    override fun getLayoutId(): Int = R.layout.fragment_tuner
    private var list_1 = arrayListOf<String>("紧五弦", "紧二五弦", "正调")
    private var list_2 = arrayListOf<String>("1", "2", "3", "4", "5", "6", "7")
    override fun lazyLoad() {
    }

    private var list: ArrayList<ArrayList<Float>>? = null
    private var currentValue: Float = 0.toFloat()
    private var minValue: Float = 0.toFloat()
    private var maxValue: Float = 0.toFloat()
    override fun getPresenter(): BasePresenter<IBaseView> = BasePresenter()
    override fun getRootView(): IBaseView = this

    override fun initViewsAndEvents() {

        picker_01.data = list_1 as List<CharSequence>?
        picker_02.data = list_2 as List<CharSequence>?


        val dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0)

        val pdh = PitchDetectionHandler { result, e ->
            val pitchInHz = result.pitch
            try {
                    ver_line.setCurrentValue(pitchInHz)
            } catch (ex: java.lang.Exception) {
            }

        }
        val p = PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050f, 1024, pdh)
        dispatcher.addAudioProcessor(p)
        try {
            mThread = Thread(dispatcher, "Audio Dispatcher")
        } catch (e: java.lang.Exception) {
        }
        var postion1: Int = 1
        var postion2: Int = 1
        list = arrayListOf()
        var list1 = arrayListOf<Float>(11f, 22f, 33f, 44f, 55f, 66f, 77f)
        var list2 = arrayListOf<Float>(110f, 220f, 330f, 440f, 550f, 660f, 770f)
        var list3 = arrayListOf<Float>(65f, 73f, 87f, 98f, 110f, 130f, 147f)
        list?.add(list1)
        list?.add(list2)
        list?.add(list3)
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

    }

    private fun setValue(p1: Int, p2: Int) {
        ver_line.setValue(list?.get(p1)!!.get(p2)-50, list?.get(p1)!!.get(p2) +50, ""+list?.get(p1)!!.get(p2).toInt())

    }

    override fun isRegistEventBus(): Boolean = false

    override fun isNeedLec(): View? = null

    companion object {
        fun newInstance(): TunerFragment {
            return TunerFragment()
        }
    }

    override fun onPause() {
        super.onPause()
        try {
            mThread?.interrupt()
        } catch (e: Exception) {
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            mThread?.start()
        }catch (e:java.lang.Exception){

        }

//        mThread?.resume()
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            mThread?.stop()
        } catch (e: Exception) {
        }

    }
}