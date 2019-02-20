package com.parkingwang.vehiclekeyboard;

import android.app.Activity;
import android.content.Context;

import com.parkingwang.vehiclekeyboard.view.InputView;
import com.parkingwang.vehiclekeyboard.view.KeyboardView;

import static com.parkingwang.vehiclekeyboard.support.PopupHelper.dismissFromActivity;
import static com.parkingwang.vehiclekeyboard.support.PopupHelper.showToActivity;

/**
 *陈渝金
 * 645503254@qq.com
 */
public class PopupKeyboard {
    
//    public abstract void showpopup();
    
    private final KeyboardView mKeyboardView;

    private KeyboardInputController mController;

    public PopupKeyboard(Context context) {
        mKeyboardView = new KeyboardView(context);
    }

    public KeyboardView getKeyboardView() {
        return mKeyboardView;
    }

    public void attach(InputView inputView, final Activity activity) {
        if (mController == null) {
            mController = KeyboardInputController
                    .with(mKeyboardView, inputView);
            mController.useDefaultMessageHandler();

            inputView.addOnItemSelectedListener(new InputView.OnItemSelectedListener() {
                @Override
                public void onSelected(int index) {
                    show(activity);
//                    showpopup();
                }
            });
        }
    }

    public KeyboardInputController getController() {
        return checkAttachedController();
    }

    public void show(Activity activity) {
        checkAttachedController();
        showToActivity(activity, mKeyboardView);
    }

    public void dismiss(Activity activity) {
        checkAttachedController();
        dismissFromActivity(activity);
    }

    public boolean isShown() {
        return mKeyboardView.isShown();
    }

    private KeyboardInputController checkAttachedController() {
        if (mController == null) {
            throw new IllegalStateException("Try attach() first");
        }
        return mController;
    }

}
