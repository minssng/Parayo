package com.example.parayo.product

import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.parayo.R
import com.example.parayo.product.list.ProductListPagerAdapter
import net.codephobia.ankomvvm.components.BaseActivity
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.setContentView

class ProductMainActivity :
        BaseActivity<ProductMainViewModel>() {

        override val viewModelType = ProductMainViewModel::class
        private val ui by lazy { ProductMainUI(getViewModel()) }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            ui.setContentView(this)
            // TabLayout과 ViewPager를 연결해주는 코드
            ui.viewpager.adapter =
                ProductListPagerAdapter(supportFragmentManager) // ViewPager의 adapter로 앞서 만든 ProductListPagerAdapter를 생성해 넣어줌.
            ui.tablayout.setupWithViewPager(ui.viewpager) // ProductMainUI에 정의된 TabLayout의 setupwithViewPager() 함수를 이용해 ViewPager와 연결.

            setupDrawerListener()
        }

        private fun setupDrawerListener() {
            // ActionBarDrawerToggle은 툴바의 햄버거버튼과 드로어 레이아웃의 이벤트를 연결시켜주는 역할을 하는  리스너 클래스.
            val toggle = ActionBarDrawerToggle(
                this,
                ui.drawerLayout,
                ui.toolBar,
                R.string.open_drawer_description,
                R.string.close_drawer_description
            )
            ui.drawerLayout.addDrawerListener(toggle) //  바로 앞에 설명한 ActionBarDrawerToggle 객체를 드로어 레이아웃의 이벤트 리스너로 등록

            toggle.syncState() // 현재 네비게이션 드로어의 상태와 함버거버튼의 상태를 동기화.
        }
}