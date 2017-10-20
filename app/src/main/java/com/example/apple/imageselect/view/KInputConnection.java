package com.example.apple.imageselect.view;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.BaseInputConnection;

/**
 * Created by apple on 17/10/20.
 */

public class KInputConnection extends BaseInputConnection {
    public KInputConnection(View targetView, boolean fullEditor) {
        super(targetView, fullEditor);
    }

    // 输入法的按键信息
    @Override
    public boolean sendKeyEvent(KeyEvent event) {
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_DEL:
                if (event.getAction() == KeyEvent.ACTION_UP
                        && onCommitTextListener != null) {
                    onCommitTextListener.onDeleteText();
                }
                break;
        }
        return super.sendKeyEvent(event);
    }

    // 输入法提交了一个 text
    @Override
    public boolean commitText(CharSequence text, int newCursorPosition) {
        if (onCommitTextListener != null) {
            onCommitTextListener.commitText(text, newCursorPosition);
        }
        return true;
    }

    private OnCommitTextListener onCommitTextListener;

    public void setOnCommitTextListener(OnCommitTextListener onCommitTextListener) {
        this.onCommitTextListener = onCommitTextListener;
    }

    public interface OnCommitTextListener {
        boolean commitText(CharSequence text, int newCursorPosition);

        void onDeleteText();
    }
}