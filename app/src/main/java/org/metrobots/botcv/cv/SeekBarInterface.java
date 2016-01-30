package org.metrobots.botcv.cv;


import android.widget.SeekBar;

public class SeekBarInterface implements SeekBar.OnSeekBarChangeListener {
    private int progress = 0;
    boolean commitedChanges = true;
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
}
