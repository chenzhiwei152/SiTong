package com.sitong.changqin.ui.fragment

import android.view.View
import be.tarsos.dsp.io.android.AudioDispatcherFactory
import be.tarsos.dsp.pitch.PitchDetectionHandler
import be.tarsos.dsp.pitch.PitchProcessor
import com.jyall.bbzf.base.BaseFragment
import com.jyall.bbzf.base.BasePresenter
import com.jyall.bbzf.base.IBaseView
import com.sevenstringedzithers.sitong.R
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
                activity?.runOnUiThread {
                    ver_line.setValue(pitchInHz - 100, pitchInHz + 100, pitchInHz)
                }
            }catch (ex:java.lang.Exception){
            }

        }
        val p = PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050f, 1024, pdh)
        dispatcher.addAudioProcessor(p)
        try {
            mThread = Thread(dispatcher, "Audio Dispatcher")
            mThread?.start()
        }catch (e:java.lang.Exception){
        }


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