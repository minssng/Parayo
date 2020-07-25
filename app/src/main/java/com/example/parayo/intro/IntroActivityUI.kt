package com.example.parayo.intro

import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import com.example.parayo.R
import org.jetbrains.anko.*

class IntroActivityUI : AnkoComponent<IntroActivity> {
    override fun createView(ui: AnkoContext<IntroActivity>): View {
        return ui.relativeLayout { // UI의 루트 레이아웃을 RelativeLayout으로 지정합니다. RelativeLayout은 레이아웃 내의 요소들에 대해 서로간의 상대적인 위치를 지정할 수 있는 레이아웃
            gravity = Gravity.CENTER // 레이아웃 내의 요소들을 화면 가운데에 정렬합니다.
            
            // PARAYO 라는 텍스트 출력
            textView("PARAYO") {
                textSize = 24f
                textColorResource = R.color.colorPrimary
                typeface = Typeface.DEFAULT_BOLD
            }
        }
    }
}