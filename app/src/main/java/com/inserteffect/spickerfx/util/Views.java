package com.inserteffect.spickerfx.util;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;

public class Views {

    public static void changeAlpha(View view, float alpha) {
        view.animate()
            .alpha(alpha)
            .setDuration(200)
            .start();
    }

    public static void disableView(View view, boolean disable) {
        view.setEnabled(!disable);

        if (view instanceof ViewGroup) {
            final ViewGroup group = (ViewGroup) view;

            for (int i = 0, size = group.getChildCount(); i < size; i++) {
                disableView(group.getChildAt(i), disable);
            }
        }
    }

    public static void fullscreen(Window window, boolean fullscreen) {
        if (fullscreen) {
            hideSystemUI(window);
        } else {
            showSystemUI(window);
        }
    }

    private static void showSystemUI(Window window) {
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    private static void hideSystemUI(Window window) {
        window.getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    public static void setChecked(CompoundButton button, boolean checked) {
        button.setChecked(checked);
    }
}
