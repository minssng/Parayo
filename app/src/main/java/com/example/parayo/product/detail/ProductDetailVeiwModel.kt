package com.example.parayo.product.detail

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.parayo.api.ParayoApi
import com.example.parayo.api.response.ApiResponse
import com.example.parayo.api.response.ProductResponse
import com.example.parayo.product.ProductStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.codephobia.ankomvvm.databinding.addAll
import net.codephobia.ankomvvm.lifecycle.BaseViewModel
import org.jetbrains.anko.error
import java.text.NumberFormat

class ProductDetailViewModel(app: Application) : BaseViewModel(app) {

    var productId: Long? = null

    val productName = MutableLiveData("-")
    val description = MutableLiveData("")
    val price = MutableLiveData("-")
    val imageUrls: MutableLiveData<MutableList<String>> =
        MutableLiveData(mutableListOf())

    // 액티비티가 실행된 후 상품 정보를 가져오기 위한 함수. suspend 함수인 API를 호출하는
    // 부분이 포함되어 있기 때문에 코루틴으로 감싸줌.
    fun loadDetail(id: Long) = viewModelScope.launch(Dispatchers.Main) {
        try {
            val response = getProduct(id)
            if (response.success && response.data != null) {
                updateViewData(response.data)
            } else {
                toast(response.message ?: "알 수 없는 오류가 발생했습니다.")
            }
        } catch (e: Exception) {
            toast(e.message ?: "알 수 없는 오류가 발생했습니다.")
        }
    }

    private suspend fun getProduct(id: Long) = try {
        ParayoApi.instance.getProduct(id)
    } catch (e: Exception) {
        error("상품 정보를 가져오는 중 오류 발생.", e)
        ApiResponse.error<ProductResponse>(
            "상품 정보를 가져오는 중 오류가 발생했습니다."
        )
    }

    // 상품 정보를 가져온 후 그 정보들을 뷰에 보여주기 위해 프로퍼티들을 수정하는 함수.
    // status 필드로 품절 여부를 판단해 품절인 경우 상품 가격 뒤에 "(품절)"이라는 문자
    // 열을 표시해주도록 함.
    private fun updateViewData(product: ProductResponse) {
        val commaSeparatedPrice =
            NumberFormat.getInstance().format(product.price)
        val soldOutString =
            if(ProductStatus.SOLD_OUT == product.status) "(품절)" else ""

        productName.value = product.name
        description.value = product.description
        price.value =
            "\${commaSeparatedPrice} $soldOutString"
        imageUrls.addAll(product.imagePaths)
    }

    // 스토리보드에서 상품 문의 버튼을 눌렀을 때 호출될 함수를 미리 정의.
    fun openInquiryActivity() {
        toast("상품 문의 - productId = $productId")
    }
}