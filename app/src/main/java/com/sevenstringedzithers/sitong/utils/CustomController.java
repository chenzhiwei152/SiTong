package com.sevenstringedzithers.sitong.utils;

import android.content.Context;

import com.sevenstringedzithers.sitong.ui.listerner.RVAdapterItemOnClick;
import com.xiao.nicevideoplayer.Clarity;
import com.xiao.nicevideoplayer.TxVideoPlayerController;

import java.util.List;

public class CustomController extends TxVideoPlayerController{
    private RVAdapterItemOnClick backListerner;
    private List<Clarity> clarities;
    private int defaultClarityIndex;

    public void setBackListerner(RVAdapterItemOnClick backListerner) {
        this.backListerner = backListerner;
    }

    public CustomController(Context context) {
        super(context);
    }

    @Override
    protected void onPlayModeChanged(int playMode) {
        super.onPlayModeChanged(playMode);
        switch (playMode){
            case 10:
                if (backListerner!=null){
                    backListerner.onItemClicked("");
                }
                break;
        }

    }


}
