package com.example.parayo.product.detail

import android.graphics.Color
import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet.PARENT_ID
import com.example.parayo.R
import net.codephobia.ankomvvm.databinding.bindItem
import net.codephobia.ankomvvm.databinding.bindString
import org.jetbrains.anko.*
import org.jetbrains.anko.constraint.layout.ConstraintSetBuilder
import org.jetbrains.anko.constraint.layout.applyConstraintSet
import org.jetbrains.anko.constraint.layout.constraintLayout
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.viewPager

class ProductDetailUI(
    private val viewModel: ProductDetailViewModel
) : AnkoComponent<ProductDetailActivity> {
    override fun createView(ui: AnkoContext<ProductDetailActivity>) =
        ui.constraintLayout {
            // 상품 이미지와 상세 정보가 노출되는 영역은 화면보다 길어 스크롤이 필요할 수 있으므로
            // ScrollView를 배치. ScrollView는 하나의 자식만 가져야 하는 특성을 가졌기 때문에
            // 바로 내부에 LinearLayout 등을 배치해 스크롤이 되어야 하는 모든 뷰는 LinearLayout 안에
            // 위치시켜야 함.
            val content = scrollView {
                id = View.generateViewId()
                lparams(matchParent, 0)

                verticalLayout {
                    constraintLayout {
                        lparams(matchParent, matchParent)
                        viewPager { // 이미지 슬라이더로 사용될 ViewPager
                            backgroundColor = Color.GRAY
                            // 앞서 작성한 ImageSliderAdapter를 ViewPager을 어댑터로 등록. 그리고 bindItem()을
                            // 통해 아이템 리스트가 변경되었을 때에 호출될 콜백에서 updateItems()를 호출. ViewPager의
                            //  bindItem() 함수는 AnkoMVVM 라이브러리에 정의된 함수로 데이터세트가 변경되었을 때
                            // 콜백을 호출해주는 역할만 함.
                            adapter = ImageSliderAdapter().apply {
                                bindItem(ui.owner, viewModel.imageUrls) {
                                    updateItems(it)
                                }
                            }
                        }.lparams(matchParent, dip(0)) {
                            // dimensionRatio는 ConstraintLayout 내부의 뷰에 대한 가로 세로 비율을 의미
                            // 여기에서는 이미지 슬라이더를 정사각형으로 만들기 위해 ConstraintLayout으로
                            // ViewPager를 감싸고 dimensionRatio를 1:1로 지정.
                            dimensionRatio = "1:1"
                        }
                    }

                    verticalLayout {
                        padding = dip(20)

                        textView {
                            textSize = 16f
                            typeface = Typeface.DEFAULT_BOLD
                            textColor = Color.BLACK
                            bindString(ui.owner, viewModel.productName)
                        }.lparams(matchParent, wrapContent)

                        textView {
                            textSize = 16f
                            typeface = Typeface.DEFAULT_BOLD
                            textColorResource = R.color.colorAccent
                            bindString(ui.owner, viewModel.price)
                        }.lparams(matchParent, wrapContent) {
                            topMargin = dip(20)
                        }

                        textView("상품설명") {
                            textSize = 16f
                            typeface = Typeface.DEFAULT_BOLD
                            textColorResource = R.color.colorPrimary
                        }.lparams(matchParent, wrapContent) {
                            topMargin = dip(20)
                        }

                        textView {
                            textSize = 14f
                            textColor = Color.BLACK
                            bindString(ui.owner, viewModel.description)
                        }.lparams(matchParent) {
                            topMargin = dip(20)
                        }

                        textView("상품설명") {
                            textSize = 16f
                            typeface = Typeface.DEFAULT_BOLD
                            textColorResource = R.color.colorPrimary
                        }.lparams(matchParent, wrapContent) {
                            topMargin = dip(20)
                        }
                    }
                }
            }

            // 스크롤과는 별개로 하단에 고정될 상품 문의 버튼이 있는 바를 정의한 부분.
            val fixedBar = linearLayout {
                id = View.generateViewId()
                padding = dip(10)
                gravity = Gravity.END
                backgroundColor = Color.DKGRAY
                lparams(matchParent, wrapContent)

                button("상품 문의") {
                    onClick { viewModel.openInquiryActivity() }
                }
            }

            // applyConstraintSet {} 내에서 ScrollView 영역과 하단 바 영역에 대한 위치를 설정.
            applyConstraintSet {
                fixedBar.id {
                    connect(
                        ConstraintSetBuilder.Side.BOTTOM to ConstraintSetBuilder.Side.BOTTOM of PARENT_ID
                    )
                }

                content.id {
                    connect(
                        ConstraintSetBuilder.Side.TOP to ConstraintSetBuilder.Side.TOP of PARENT_ID,
                        ConstraintSetBuilder.Side.BOTTOM to ConstraintSetBuilder.Side.TOP of fixedBar
                    )
                }
            }
        }
}