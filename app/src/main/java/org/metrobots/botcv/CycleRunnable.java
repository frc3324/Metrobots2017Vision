package org.metrobots.botcv;

import android.widget.EditText;

import java.math.BigInteger;

/**
 * Created by Tasgo on 1/15/16.
 */
public class CycleRunnable implements Runnable {
    private EditText editText;
    private BigInteger data = BigInteger.ZERO;
    public boolean paused = false;

    public CycleRunnable(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void run() {
        while (true) {
            if (!paused) {
                increment();
                final String text = data.toString(36);
                editText.post(new Runnable() {
                    @Override
                    public void run() {
                        editText.setText(text);
                    }
                });
            }
            try {
                Thread.sleep(20);
            } catch (InterruptedException ignored) { }
        }
    }

    public void increment() {
        data = data.add(BigInteger.ONE);
    }
}
