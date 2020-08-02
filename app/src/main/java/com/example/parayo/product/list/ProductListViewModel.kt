package com.example.parayo.product.list

import android.app.Application
import android.content.Intent
import androidx.paging.DataSource
import com.example.parayo.api.response.ProductListItemResponse
import com.example.parayo.product.detail.ProductDetailActivity
import net.codephobia.ankomvvm.lifecycle.BaseViewModel
import org.jetbrains.anko.error

class ProductListViewModel(
    app: Application
) : BaseViewModel(app),
    ProductListPagedAdapter.ProductLiveDataBuilder,
    ProductListPagedAdapter.OnItemClickListener {

    // 각 Fagment에서 표시되어야 할 아이템들의 카테고리 id입니다. -1인 경우
    var categoryId: Int = -1
    // 데이터소스로부터 아이템 리스트를 어떻게 가져올 것인지에 대한 설정. 페이지는
    // 사이즈는 10으로 설정.
    val products = buildPagedList()

    // ProductListPagedAdapter.ProductLiveDataBuilder를 구현했을 때 작성해주어야 하는
    // 함수로 상품 목록에 필요한 ProductListItemDataSource를 반환하고 있음.
    override fun createDataSource(): DataSource<Long, ProductListItemResponse> {
        if (categoryId == -1) {
            error(
                "categoryId가 설정되지 않았습니다.",
                IllegalStateException("categoryId is -1")
            )
        }
        
        return ProductListItemDataSource(categoryId)
    }
    
    // ProductListPagedAdapter.OnItemClickListener를 구현해 상품 목록에서 상품을 클릭
    // 했을 때 동작할 리스너를 구현.
    override fun onClickProduct(productId: Long?) {
//        startActivity<ProductDetailActivity> {
//            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
//            putExtra(ProductDetailActivity.PRODUCT_ID, productId)
//        }
    }

    fun onClickItem(id: Long?) {
        startActivity<ProductDetailActivity> {
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
            putExtra(ProductDetailActivity.PRODUCT_ID, id)
        }
     }
}