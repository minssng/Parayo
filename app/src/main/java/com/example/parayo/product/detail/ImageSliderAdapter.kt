package com.example.parayo.product.detail

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.parayo.api.ApiGenerator
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.imageView

class ImageSliderAdapter : PagerAdapter() { // PagerAdapter를 상속
    var imageUrls: List<String> = listOf()

    // ViewPager에서 현재 위치한 페이지가 instantiateItem()으로부터 반환된 뷰인지 비교하는 함수
    // PagerAdapter가 정상적으로 동작하기 위해서 꼭 구현해주어야 하는 함수.
    override fun isViewFromObject(view: View, obj: Any) =
        view == obj

    override fun getCount() = imageUrls.size

    // 실질적으로 우리가 원하는 뷰를 만들어 반환하는 함수. 원하는 뷰가 이미지 한 개 짜리
    // 이기 때문에 별도 UI 클래스를 만들지 않고 함수 안에 바로 작성.
    override fun instantiateItem(
        container: ViewGroup,
        position: Int
    ): Any {
        val view = AnkoContext.create(container.context, container)
            .imageView().apply {
                Glide.with(this)
                    .load("${ApiGenerator.HOST}${imageUrls[position]}")
                    .into(this)
            }
        container.addView(view)
        return view
    }

    // 빈 리스트를 API로부터 받아온 이미지로 교체해주기 위해 마련한 함수. 아이템이
    // 엡데이트된 이후에는 notifyDataSetChanged() 함수를 호출해주어야만 아이템들이
    // 뷰에 정상적으로 반영하게 됨.
    fun updateItems(items: MutableList<String>) {
        imageUrls = items
        notifyDataSetChanged()
    }

    // ViewPager는 현재 페이지와 좌우에 이웃한 페이지만 생성하고 이외에는 페이지를 삭제하기
    // 때문에 이 때 뷰를 제거해주는 것은 개발자의 몫. 때문에 destroyItem() 내부에서
    // container.invalidate() 함수를 호출해 컨테이너에 추가했던 뷰를 제거.
    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        obj: Any
    ) {
        container.invalidate()
    }
}