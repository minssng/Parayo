package com.example.parayo.product.list

import androidx.paging.PageKeyedDataSource
import com.example.parayo.App
import com.example.parayo.api.ParayoApi
import com.example.parayo.api.response.ApiResponse
import com.example.parayo.api.response.ProductListItemResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.anko.toast

// PageKeyedDataSource를 상속받는 클래스. 이 클래스는 초기 데이터를
// 로드하고 보다 이전/이후의 데이터를 로드하기 위한 콜백으로 구성
class ProductListItemDataSource(
    private val categoryId: Int?
) : PageKeyedDataSource<Long, ProductListItemResponse>() {
    // loadInitial() 콜백은 초기 데이터를 로드하는 콜백입니다. 상품을 최신 순으로읽어와야하기
    // 때문에 id로는 Long.MAX_VALUE를 사용
    override fun loadInitial(
        params: LoadInitialParams<Long>,
        callback: LoadInitialCallback<Long, ProductListItemResponse>
    ) {
        val response = getProducts(Long.MAX_VALUE, NEXT)
        if (response.success) {
            response.data?.let {
                if (it.isNotEmpty()) {
                    // API로부터 데이터를 받았다면 callback.onResult()를 호출해 데이터가 추가되었음을 알려야 함.
                    // loadInitial()의 파라미터인 LoadInitialCalback의 onResult()는 두 번째와 세 번째 파라미터로
                    // API로부터 받은 첫 번째 데이터의 id와 마지막 데이터의 id를 넣어주게 되어 있음. 두 번째 파라미터
                    // id는 loadBefore() 콜백에서 더 최신 데이터를 읽어오기 위해 사용. 세 번째 파라미터는 id는 loadAfter()
                    // 에서 보다 과거 데이터를 가져오기 위해 사용.
                    callback.onResult(it, it.first().id, it.last().id)
                }
            }
        } else {
            GlobalScope.launch(Dispatchers.Main) { // 콜백이 UI 스레드에서 실행되지 않기 때문에 토스트 메시지를 보여주기 위해서는 메인 스레드로 변경 후 호출해야 함.
                showErrorMessage(response)
            }
        }
    }

    // 다음(과거) 데이터를 불러오기 위해서 사용됨.
    override fun loadAfter(
        params: LoadParams<Long>,
        callback: LoadCallback<Long, ProductListItemResponse>
    ) {
        val response = getProducts(params.key, NEXT)
        if (response.success) {
            response.data?.let {
                if (it.isNotEmpty()) {
                    // loadAfter()의 callback.onResult()에서는 다음(더 이전)의 목록을 불러오기 위해 두 번째
                    // 파라미터로 API로부터 받은 마지막 데이터의 id를 넘겨줌.
                    callback.onResult(it, it.last().id)
                }
            }
        } else {
            GlobalScope.launch(Dispatchers.Main) {
                showErrorMessage(response)
            }
        }
    }

    // loadBefore()의 callback.onResult()에서는 더 이전의 목록을 불러오기 위해 두 번째 파라미터로 
    // API로 부터 받은 첫 번째 데이터의 id를 넘겨줌
    override fun loadBefore(
        params: LoadParams<Long>,
        callback: LoadCallback<Long, ProductListItemResponse>
    ) {
        val response = getProducts(params.key, PREV)
        if (response.success) {
            response.data?.let {
                if (it.isNotEmpty()) {
                    // loadBefore()의 callback.onResult()에서는 더 이전의 목록을 불러오기 위해 두 번째 파라미터로 API
                    // 로부터 받은 첫 번째 데이터의 id를 넘겨줌.
                    callback.onResult(it, it.first().id)
                }
            }
        } else {
            GlobalScope.launch(Dispatchers.Main) {
                showErrorMessage(response)
            }
        }
    }

    private fun getProducts(id: Long, direction: String) = runBlocking {
        try {
            ParayoApi.instance.getProducts(id, categoryId, direction)
        } catch (e: Exception) {
            ApiResponse.error<List<ProductListItemResponse>>(
                "알 수 없는 오류가 발생했습니다."
            )
        }
    }

    private fun showErrorMessage(
        response: ApiResponse<List<ProductListItemResponse>>
    ) {
        App.instance.toast(
            response.message ?: "알 수 없는 오류가 발생했습니다."
        )
    }

    companion object {
        private const val NEXT = "next"
        private const val PREV = "prev"
    }
}