package com.example.framework.base;

import android.os.Bundle;

import com.example.framework.utils.SystemUI;

/**
 * FileName: BaseUIActivity
 * Founder: LiuGuiLin
 * Profile: UI 基类
 */
public class BaseUIActivity extends BaseActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 内部制作了沉浸式UI栏的封装
         */
        SystemUI.fixSystemUI(this);
    }
}