package com.example.parayo.product.detail

import android.os.Bundle
import android.view.MenuItem
import net.codephobia.ankomvvm.components.BaseActivity
import org.jetbrains.anko.setContentView

class ProductDetailActivity : BaseActivity<ProductDetailViewModel>() {
    override val viewModelType = ProductDetailViewModel::class

    override fun onCreate(savedInstanceSate: Bundle?) {
        super.onCreate(savedInstanceSate)
        // 앱바 영역의 좌측 버튼 아이콘을 뒤로가기 버튼으로 만들어줌. 앱반의 타이틀은
        // 필요하지 않으므로 빈 문자열을 지정.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = ""

        val viewModel = getViewModel()
        // 액티비티를 열 때에는 Intent를 통해 데이터를 전달할 수 있음. ProductDetailActivity에서는
        // "productId"라는 키로 Long 타입의 id를 받을 생각으로 코드를 작성했으므로 ProductListViewModel
        // 에서 ProductDetailActivity를 시작할 때 동일한 키로 데이터를 넘겨주어야 함. 넘겨주지 않을 경우에는
        // 디폴트 값으로 -1을 사용하게 되며 이 경우 서버에서 "상품 정보를 찾을 수 없습니다."라는 메시지를
        // 반환하기 때문에 사용자는 해당 오류 메시지를 확인할 수 있게 됨.
        val productId = intent.getLongExtra(PRODUCT_ID, -1)

        viewModel.loadDetail(productId) // 액티비티가 시작될 때 ProductDetailViewModel의 loadDetail() 함수를 호출해 상품 정보를 로드.
        ProductDetailUI(getViewModel()).setContentView(this)
    }

    // 앱바에서 뒤로가기 버튼을 눌렀을 때 상품 상세 화면을 닫도록 onOptionsItemSelected() 함수에
    // android.R.id.home 버튼 이벤트를 추가.
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        item?.let {
            when(item.itemId) {
                android.R.id.home -> onBackPressed()
                else -> {}
            }
        }
        return true
    }

    companion object {
        val PRODUCT_ID = "productId"
    }
}