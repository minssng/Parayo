package com.example.parayo.product.list

import android.graphics.Typeface
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintProperties.PARENT_ID
import com.example.parayo.R
import com.example.parayo.view.borderBottom
import org.jetbrains.anko.*
import org.jetbrains.anko.constraint.layout.ConstraintSetBuilder
import org.jetbrains.anko.constraint.layout.applyConstraintSet
import org.jetbrains.anko.constraint.layout.constraintLayout

class ProductListItemUI : AnkoComponent<ViewGroup> {
    // ProductListItemUI를 사용할 ViewHolder 클래스에서 이미지 및 상품명, 가격을 변경할
    // 수 있도록 별개의 프로퍼티로 선언.
    lateinit var imageView: ImageView
    lateinit var productName: TextView
    lateinit var price: TextView

    override fun createView(ui: AnkoContext<ViewGroup>) =
        // ConstraintLayout은 컨테이너 내의 상대적인 위치를 지정하기 쉽도록 만든 레이아웃.
        // 레이아웃 안의 각 요소에 상대적인 위치를 선언하는 방법도 있지만 여기에서처럼
        // applyConstaintSet{} 블록으로 따로 빼내어 조금 더 구조적으로 작성할 수도 있음.
        ui.constraintLayout {
            topPadding = dip(20)
            bottomPadding = dip(20)
            clipToOutline = false
            background = borderBottom(width = 1)
            lparams(matchParent, wrapContent)

            imageView = imageView {
                // ConstraintLayout 안에서 상대적인 위치를 지정하기 위해서는 각 요소가 id를
                // 가지고 있어야 함.
                id = View.generateViewId()
                scaleType = ImageView.ScaleType.CENTER_CROP
            }.lparams(dip(80), dip(80))

            productName = textView("-") {
                id = View.generateViewId()
                textSize = 16f
                typeface = Typeface.DEFAULT_BOLD
                textAlignment = TextView.TEXT_ALIGNMENT_VIEW_START
            }.lparams(0, wrapContent)

            price = textView("-") {
                id = View.generateViewId()
                textColorResource = R.color.colorAccent
                textSize = 14f
            }

            // applyConstraintSet {} 블록 안에서는 id를 가진 뷰들의 상대적인 위치를 지정할
            // 수 있음. 예를 들어 imageView에 TOP to TOP of PARENT_ID를 선언하면 이미지의
            // 위쪽이 부모 컨테이너의 윗쪽에 붙어야 한다는 것을 나타냄. 마찬가지로 productName에
            // TOP to TOP of imageView.id margin dip(4)를 선언한 것은 productName의 상단이
            // imageView의 상단을 기준으로 4dp 떨어져서 위치해야 한다는 것을 나타냄.
            applyConstraintSet {
                imageView.id {
                    connect(
                        ConstraintSetBuilder.Side.TOP to ConstraintSetBuilder.Side.TOP of PARENT_ID,
                        ConstraintSetBuilder.Side.START to ConstraintSetBuilder.Side.START of PARENT_ID margin dip(20),
                        ConstraintSetBuilder.Side.BOTTOM to ConstraintSetBuilder.Side.BOTTOM of PARENT_ID
                    )
                }

                productName.id {
                    connect(
                        ConstraintSetBuilder.Side.TOP to ConstraintSetBuilder.Side.TOP of imageView.id margin dip(4),
                        ConstraintSetBuilder.Side.END to ConstraintSetBuilder.Side.END of PARENT_ID margin dip(20),
                        ConstraintSetBuilder.Side.START to ConstraintSetBuilder.Side.END of imageView.id margin dip(10)

                    )
                }

                price.id {
                    connect(
                        ConstraintSetBuilder.Side.TOP to ConstraintSetBuilder.Side.BOTTOM of productName.id margin dip(4),
                        ConstraintSetBuilder.Side.START to ConstraintSetBuilder.Side.END of imageView.id margin dip(10)
                    )
                }
            }
        }
}