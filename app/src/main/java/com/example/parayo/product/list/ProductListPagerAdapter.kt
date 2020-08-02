package com.example.parayo.product.list

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.parayo.product.category.categoryList

// ViewPager에서 Fragment를 보여줄 수 있도록 FragmentStatePagerAdapter를 상속 받는
// 클래스를 구현해줍니다. FragmentStatePagerAdapter는 생성자의 파라미터로 FragmentManager가
// 필요합니다. 이 FragmentManager는 FragmentActivity로부터 가져올 수 있음.
class ProductListPagerAdapter(
    fragmentManager: FragmentManager
) : FragmentStatePagerAdapter(
    fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT // BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT는 현재 Fragment만 라이프 사이클의 RESUMED 상태에 있을 수 있다는 것을 의미. 나머지 Fragment들은 STARTED 상태만 가질 수 있음.
) {
    // ViewPager에서 보여줄 Fragment의 리스트. 카테고리 리스트와 1:1이 되도록
    // 카테고리만큼 생성해 id와 title을 부여해줌.
    private val fragments = categoryList.map {
        ProductListFragment.newInstance(it.id, it.name)
    }

    // 지정된 위치의 Fragment를 반환하는 함수. FragmentStatePagerAdapter에 정의된
    // 추상 함수로 꼭 구현해줘야 하는 부분.
    override fun getItem(position: Int) = fragments[position]

    // Fragment의 수를 반환하는 함수. 꼭 구현해줘야 하는 추상 함수
    override fun getCount() = fragments.size

    // Fragment의 title을 반환하는 함수. 상위 클래스에서 null을 반환하도록 디폴트 구현이
    // 존재하고 일반적으로 null을 반환해도 괜찮지만 TabLayout과 함께 사용하면 탭의 이름으로
    // 사용되기 때문에 title을 반환해주도록 구현.
    override fun getPageTitle(position: Int) =
        getItem(position).title
}