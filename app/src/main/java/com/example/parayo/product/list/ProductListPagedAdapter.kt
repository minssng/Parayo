package com.example.parayo.product.list

import android.icu.text.NumberFormat
import android.os.Build
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.parayo.api.ApiGenerator
import com.example.parayo.api.response.ProductListItemResponse
import com.example.parayo.common.paging.LiveDataPagedListBuilder
import com.example.parayo.product.ProductStatus
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.sdk27.coroutines.onClick

// PagedListAdapter를 상속받아 ProductListPagedAdapter 클래스를 만들어 줌
class ProductListPagedAdapter(
    private val listener: OnItemClickListener
) : PagedListAdapter<ProductListItemResponse,
        ProductListPagedAdapter.ProductItemViewHolder> (
    // PagedListAdapter는 동일한 데이터의 경우 뷰를 다시 그려주는 낭비를 피하기 위해 생성자에서
    // 객체의 동일성을 검사하기 위한 콜백을 받습니다. 이 콜백은 5번 항목의 companion object에 정의.
    DIFF_CALLBACK
) {
    // onCreateViewHolder() 콜백은 RecyclerView가 새 ViewHolder를 요구했을 때 호출되는
    // 콜백
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = ProductItemViewHolder(parent, listener)

    // onBindViewHolder() 콜백은 RecyclerView가 특정 위치의 데이터를 출력해주려 할 때
    // 호출되는 콜백으로, ProductItemViewHolder에서 가지고 있는 itemView에 데이터를
    // 표시해주기 위해 미리 작성한 ProductItemViewHolder.bind() 함수를 호출
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(
        holder: ProductItemViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    // ProductListItemUI를 가지고 있어야 하는 ViewHolder 클래스. RecyclerView.
    // ViewHolder를 상속받으며 ViewHolder의 생성자에 ProductListItemUI의 인스턴스를
    // 넘겨주도록 했음. 이렇게 넘겨준 뷰는 이후 내부에서 itemView라는 프로퍼티로 사용.
    class ProductItemViewHolder(
        parent: ViewGroup,
        private val listener: OnItemClickListener,
        private val ui: ProductListItemUI = ProductListItemUI()
    ) : RecyclerView.ViewHolder(
        ui.createView(AnkoContext.create(parent.context, parent))
    ) {
        var productId: Long? = null

        init {
            // ViewHolder를 생성하면 클릭 리스너를 통해 OnItemClickListener의 onClickProduct()
            // 함수를 호출하도록 해줌. 이 때 productId를 함께 넣어줌
            itemView.onClick { listener.onClickProduct(productId) }
        }

        // RecyclerView가 화면에 아이템을 표시해줄 때 어댑터의 onBindViewHolder() 콜백에서
        // 호출되도록 만든 함수. ProductListItemUI의 이미지와 상품명 그리고 가격을 데이터의 값으로 변경.
        @RequiresApi(Build.VERSION_CODES.N)
        fun bind(item: ProductListItemResponse?) = item?.let {
            this.productId = item.id
            val soldOutString =
                if(ProductStatus.SOLD_OUT == item.status) "(품절)" else ""
            val commaSeparatedPrice =
                NumberFormat.getNumberInstance().format(item.price)

            ui.productName.text = item.name
            ui.price.text = "\$commaSeparatedPrice $soldOutString"

            Glide.with(ui.imageView)
                .load("${ApiGenerator.HOST}${item.imagePaths.firstOrNull()}")
                .centerCrop()
                .into(ui.imageView)
        }
    }

    companion object {
        // PagedListAdapter에서 사용될 DiffUtil.ItemCallback 정의. 아이템이 동일한지,
        //
        val DIFF_CALLBACK =
            object : DiffUtil.ItemCallback<ProductListItemResponse>() {
                override fun areItemsTheSame(
                    oldItem: ProductListItemResponse,
                    newItem: ProductListItemResponse
                ) = oldItem.id == newItem.id

                override fun areContentsTheSame(
                    oldItem: ProductListItemResponse,
                    newItem: ProductListItemResponse
                ) = oldItem.toString() == newItem.toString()
            }
    }

    // 9
    interface OnItemClickListener {
        fun onClickProduct(productId: Long?)
    }

    // 10
    interface ProductLiveDataBuilder
        : LiveDataPagedListBuilder<Long, ProductListItemResponse>
}