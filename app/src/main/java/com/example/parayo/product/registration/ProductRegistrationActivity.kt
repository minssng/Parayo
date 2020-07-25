package com.example.parayo.product.registration

import android.os.Bundle
import android.view.MenuItem
import net.codephobia.ankomvvm.components.BaseActivity
import org.jetbrains.anko.setContentView

class ProductRegistrationActivity
    : BaseActivity<ProductRegistrationViewModel>() {

    override val viewModelType = ProductRegistrationViewModel::class

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ProductRegistrationUI(getViewModel())
            .setContentView(this)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "상품 등록" // 상단 액션바에 표시될 제목 텍스트
    }

    // ProductRegistrationActivity에는 UI에서 따로 툴바를 만들지 않을 예정이기 때문에
    // onOptionsItemSelected() 콜백을 이용해 기본 액션바의 좌측 버튼이  눌렀을 때 액션을
    // 정의해주었음. 여기에서 android.R.id.home은 기본 액션바를 사용할 때 액션바 좌측의
    // 버튼 아이디를 지칭함. 이 버튼이 눌렸을 때 시스템의 백버튼을 눌렀을 때와 같은 효과를
    // 내기 위해 onBackPressed() 함수를 호출.
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when(item.itemId) {
                android.R.id.home -> onBackPressed()
                else -> {}
            }
        }
        return true
    }
}