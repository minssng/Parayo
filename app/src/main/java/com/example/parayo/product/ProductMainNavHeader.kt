package com.example.parayo.product

import android.graphics.Typeface
import android.view.View
import com.example.parayo.R
import com.example.parayo.common.Prefs
import org.jetbrains.anko.*

class ProductMainNavHeader : AnkoComponent<View> {

    override fun createView(ui: AnkoContext<View>) =
        ui.verticalLayout {
            padding = dip(20)
//            background = borderBottom(width = dip(1))

            imageView(R.drawable.ic_baseline_search_24)
                .lparams(dip(54), dip(54))

            textView(Prefs.userName) { // textView에는 로그인 후 SharedPreferences에 저장했던 userName의 값을 넣어줌.
                topPadding = dip(8)
                textSize = 20f
                typeface = Typeface.DEFAULT_BOLD
            }
        }
}