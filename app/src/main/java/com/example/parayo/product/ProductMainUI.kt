package com.example.parayo.product

import android.view.Gravity
import android.view.Menu.NONE
import android.view.MenuItem
import android.view.MenuItem.SHOW_AS_ACTION_ALWAYS
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat.generateViewId
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager.widget.ViewPager
import com.example.parayo.R
import com.example.parayo.common.Prefs
import com.example.parayo.signin.SigninActivity
import com.google.android.material.navigation.NavigationView
import org.jetbrains.anko.*
import org.jetbrains.anko.appcompat.v7.toolbar
import org.jetbrains.anko.design.navigationView
import org.jetbrains.anko.support.v4.drawerLayout
import com.example.parayo.view.borderBottom
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.GRAVITY_FILL
import com.google.android.material.tabs.TabLayout.MODE_SCROLLABLE
import org.jetbrains.anko.design.floatingActionButton
import org.jetbrains.anko.design.themedTabLayout
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.viewPager

class ProductMainUI (
    private val viewModel: ProductMainViewModel
) : AnkoComponent<ProductMainActivity>,
    NavigationView.OnNavigationItemSelectedListener { // 네비게이션의 메뉴가 클릭되었을 때 호출될 함수를 정의한 인터페이스. ProductMainUI에서 이 리스너를 상숑받고 onNavigationItemSelected()를 구현해 메뉴아이템이 클릭되었을 때 동작 해야할 로직을 정의할 수 있음.

    lateinit var navigationView: NavigationView // 초기화는 나중에 하는데 null이면 안돼서. 메뉴가 클릭되었을 때 네비게이션 드로어를 닫기 위해 NavigationView의 객체를 참조할 필요가 있기 때문에 ProductMainUI 클래스의 프로퍼티로 선언함.
    lateinit var toolBar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var tablayout: TabLayout // TabLayout을 ProductMainActivity에서 사용하기 위해 별도의 프로퍼티로 선언
    lateinit var viewpager: ViewPager // ViewPager를 ProductMainActivity에서 사용하기 위해 별도의 프로퍼티로 선언

    override fun createView(ui: AnkoContext<ProductMainActivity>) =
        ui.drawerLayout {
            drawerLayout = this

            frameLayout {
                verticalLayout { // verticalLayout() 함수는 LinearLayout에 orientation 속성을 vertical로 설정해 세로로 배열되는 LinearLayout을 생성하는 함수.
                    toolBar = toolbar {
                        title = "Parayo"
                        bottomPadding = dip(1) // 툴바의 우측 메뉴 아이콘  영역이 툴바 전체 높이를 덮어버리기 때문에 하단 배경은 잘리는 현상이 있어 툴바의 하단에 1DP의 여백을 주었음.
                        background = borderBottom(width = dip(1)) //
                        menu.add("Search") // 툴바의 우측  메뉴를 추가하는 코드.
                            .setIcon(R.drawable.ic_baseline_search_24)
                            .setShowAsAction(SHOW_AS_ACTION_ALWAYS) // 일반적으로 툴바에서 menu.add(...) 로 추가된 메뉴는 툴바에 아이콘으로 나타나지 않고 팝업  메뉴 안에 나타나게 됨. 툴바에 직접 표시해주기 위해서는 SHOW_AS_ACTION_ALWAYS로 설정.

                        // TabLayout을 생성하는 함수와 빌더 블록. tabLayout {...}을 사용할 수도 있지만
                        // 테마를 지정해주면 디자인을 변경할 수 있기 때문에 기본 메터리얼 디자인을 가져다
                        // 사용하기 위해 테마의 리소스 아이디를 파라미터로 받는 themedTabLayout() 함수를 사용.
                        tablayout = themedTabLayout(
                            R.style.Widget_MaterialComponents_TabLayout
                        ) {
                            bottomPadding = dip(1)
                            tabMode = MODE_SCROLLABLE // 화면 범위를 넘어가게 될 경우 스크롤하게 만들지 탭 크기를 줄여 컨텐츠를 화면 사이즈에 맞출지를 결정. MODE_SCROLLABLE은 탭 크기를 유지하면서 스크롤할 수 있게 하며 MODE_FIXED는 탭 크기를 줄여 스크롤 없이 화면 사이즈에 탭을 맞춤.
                            tabGravity = GRAVITY_FILL // 탭 영역이 상위 컨테이너를 채울지 컨텐츠 크기에 맞출지를 결정. GRAVITY_FILL은 상위 컨테이너를 가득 채우고 GRAVITY_CENTER는 컨텐츠 사이즈에 맞춰 가운데 정렬을 함. 4번 항목의 tabMode가 MODE_SCROLLABLE일 경우에는 tabGravity 값이 GRAVITY_CENTER일지라도 좌측 정렬이 됨
                            background = borderBottom(width = dip(1))
                            lparams(matchParent, wrapContent)
                        }

                        viewpager = viewPager {
                            id = generateViewId() // ViewPager를 TabLayout과 연동하는 경우 id 값이 필수이기 때문에 View, generateViewId() 함수로 아이디를 생성.
                        }.lparams(matchParent, matchParent)
                    }
                }

                floatingActionButton {
                    imageResource = R.drawable.plus
                    onClick { viewModel.openRegistrationActivity() }
                }.lparams {
                    bottomMargin = dip(20)
                    marginEnd = dip(20)
                    gravity = Gravity.END or Gravity.BOTTOM
                }
            }

            // 3 navigationView 함수를 이용해 네비게이션 드로어를 생성해줌.
            navigationView = navigationView { //
                ProductMainNavHeader()
                    .createView(AnkoContext.create(context, this)) // AnkoContext.create(...) 는 새 AnkoContext를 생성하는 정적 함수. 일반적인 컨테이너들과 달리 NavigationView에 헤더 뷰를 삽입하기 위해서는 addHeaderView(view) 함수를 사용해야 하기 때문에 AnkoContext.create(...) 함수로 별도 UI 컨텍스트에서 뷰를 생성해 addHeaderView의 인자에 넘겨주는 방법을 사용함.
                    .let(::addHeaderView) // .run(::addHeaderView)는 let, apply, run 등의 함수를 체이닝하는 일반적인 사용 방법 중 하나. run { addHeaderView(this) } 과 같이 람다를 넘겨줄 수도 있지만 파라미터가 하나일 경우 run(::addHeaderView)와 같이 함수 레퍼런스를 넘기는 것도 허용되므로 더 간략하게 코드를 작성할 수 잇음.
                // menu.add() 함수의 파라미터는 순서대로 groupId, itemId, order, title. goupId와 order는 특정할 필요가 없으면 Menu.NONE으로 지정해주면 됨. groupId를 1 이상의 것으로 특정해주면 같은 groupId를 가진 메뉴들끼리 그룹핑되어 표시되게 됨.
                menu.apply {
                    add(NONE, MENU_ID_INQUIRY, NONE, "내 문의").apply {
                        setIcon(R.drawable.ic_baseline_search_24)
                    }
                    add(NONE, MENU_ID_LOGOUT, NONE, "로그아웃").apply {
                        setIcon(R.drawable.ic_baseline_search_24)
                    }
                }

                // 메뉴가 클릭되었을 때 동작할 함수를 정의한 ONNavigationItemSelectedListener 구현체를 지정해주는 함수. 여기에서는 ProductMainUI가 해당 리스너를 구현했으므로 this@ProductMainUI를 지정해줌. 이 블록(navigationView{...}) 내부에서는 this가 NavigationView  객체를 의미하므로 @ProductMainUI를 붙여 ProductMainUI 객체를 지정하도록 함.
                setNavigationItemSelectedListener(this@ProductMainUI)
            }.lparams(wrapContent, matchParent) {
                gravity = Gravity.START // 4 lparams 블록 안의 gravity 속성은 해당 뷰가 컨테이너보다 작은 경우 컨테이너의 어느쪽으로 정렬될 것인지를 나타냄. navigationView의 경우 drawerLayout의 안쪽에서 좌측에 정렬되어 나타나야 하므로 gravity를 START로 설정했습니다.
            }

            // floatingActionButton은 기본적으로  UI 최상단에 떠있는 원형의 버튼.
            // gravity 속성을 이용해 FrameLayout에서의 위치를 우측 하단으로 지정해주고 마진
            // 을 설정해 세밀한 위치를 잡아주었습니다. 아이콘으로 사용된 R.drawable.ic_add는
            // materialdesignicons.com에서 plus 아이콘을 xml로 다운받아 연 후 fillColor 속성만
            // #FFF로 변경해줌. 이렇게 추가한 버튼은 다음과 같이 보이게 됨.
            floatingActionButton {
                imageResource = R.drawable.ic_baseline_search_24
                onClick { viewModel.openRegistrationActivity() }
            }.lparams {
                bottomMargin = dip(20)
                marginEnd = dip(20)
                gravity = Gravity.END or Gravity.BOTTOM
            }
        }

    // OnNavigationItemSelectedListener의 구현부. when 절을 이용해 메뉴아이템의
    // itemId 값을 기준으로 수행해야 할 로직을 구분해주었음. 아직은 문의 메뉴를
    // 눌렀을 때 이동할 화면이 만들어지지 않았으므로 토스트를 띄우는 것으로 대체.
    // 함수의 맞미막에서는 drawerLayout.closeDrawer()를 이용해 네비게이션
    // 드로어를 닫아줌. 이렇게 하지 않으면 화면이 전환되었다가 다시 돌아왔을 때
    // 여전히 열려있는 네비게이션 드로어를 보게 됨.
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            MENU_ID_INQUIRY -> { viewModel.toast("내 문의") }
            MENU_ID_LOGOUT -> {
                Prefs.token = null
                Prefs.refreshToken = null
                viewModel.startActivityAndFinish<SigninActivity>()
            }
        }

        drawerLayout.closeDrawer(navigationView)

        return true
    }

    // 메뉴를 클릭했을 때에는 메뉴의 ID 값으로 케이스를 구분할 필요가 있어 각 메뉴에 아이디를 부여해주어야 함. companion object {} 안에 ID 값들을 선언해줌. 그리고 menu.add()의 파라미터로 각 ID를 넘겨주어야 함.
    companion object {
        private const val MENU_ID_INQUIRY = 1
        private const val MENU_ID_LOGOUT = 2
    }
}