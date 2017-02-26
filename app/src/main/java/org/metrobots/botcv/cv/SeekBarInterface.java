package org.metrobots.botcv.cv;


import android.widget.SeekBar;

public class SeekBarInterface implements SeekBar.OnSeekBarChangeListener {
    private int progress = 0;
    boolean commitedChanges = true;

    public SeekBarInterface(int s){
        progress = s;
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        this.progress = progress;
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int s){
        progress = s;
    }
}
