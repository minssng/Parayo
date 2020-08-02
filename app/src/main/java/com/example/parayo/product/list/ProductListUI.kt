package com.example.parayo.product.list

import android.view.Gravity
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.LinearLayoutManager
import net.codephobia.ankomvvm.databinding.bindPagedList
import net.codephobia.ankomvvm.databinding.bindVisibility
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

class ProductListUI (
    private val viewModel: ProductListViewModel
) : AnkoComponent<ProductListFragment> {

    override fun createView(ui: AnkoContext<ProductListFragment>) =
        ui.verticalLayout {
            // 아이템 리스트를 표시해주기 위한 RecyclerView 블럭
            recyclerView {
                // RecyclerView에는 아이템들을 어떻게 배열할 것인지에 대한 LayoutManager를
                // 설정해주어야 함. LinearLayoutManager는 아이템들을 일렬로 배열하고
                // GridLayoutManager는 그리드 형태로 배열함.
                layoutManager = LinearLayoutManager(ui.ctx)

                // 어댑터로는 앞서 만든 ProductListPageAdapter의 객체를 사용.
                adapter = ProductListPagedAdapter(viewModel)

                lparams(matchParent, matchParent)

                // 상품이 없을 경우 Recyclerview를 숨기고 상품이 없다는 메시지를 보여주기 위해
                // bindVisibility() 함수의 콜백으로 products가 비어있지 않은지 여부를 반환합니다.
                bindVisibility(ui.owner, viewModel.products) {
                    it.isNotEmpty()
                }

                bindPagedList( // LiveData<PagedList<T>> 타입의 객체를 바인딩하는 함수.
                    ui.owner,
                    ProductListPagedAdapter(viewModel),
                    viewModel.products
                )
            }
            textView("상품이 없습니다.") { // 6
                gravity = Gravity.CENTER
                bindVisibility(ui.owner, viewModel.products) {
                    it.isEmpty()
                }
            }.lparams(wrapContent, matchParent) {
                gravity = Gravity.CENTER
            }
        }
}