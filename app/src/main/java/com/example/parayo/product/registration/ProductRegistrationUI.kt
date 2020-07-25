package com.example.parayo.product.registration

import android.graphics.Color
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.example.parayo.R
import com.example.parayo.api.ApiGenerator
import net.codephobia.ankomvvm.databinding.bindString
import net.codephobia.ankomvvm.databinding.bindStringEntries
import net.codephobia.ankomvvm.databinding.bindUrl
import org.jetbrains.anko.*
import org.jetbrains.anko.design.textInputEditText
import org.jetbrains.anko.design.textInputLayout
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.sdk27.coroutines.onItemSelectedListener
import org.jetbrains.anko.sdk27.coroutines.textChangedListener

class ProductRegistrationUI(
    private val viewModel: ProductRegistrationViewModel
) : AnkoComponent<ProductRegistrationActivity> {

    override fun createView(
        ui: AnkoContext<ProductRegistrationActivity>
    ) = ui.scrollView { // 상품 등록 화면은 세로로 길기 때문에 스크롤이 필요해  scrollView를 최상위 레이아웃으로 두고 그 안에 verticalLayout을 넣음
        verticalLayout {
            padding = dip(20)
            clipToPadding = false // 컨테이너의 패딩 영역을 넘어서 위치한 자식 뷰가 시각적으로 보여지도록 할지를 결정. 예를 들어 영역을 가득 채운 버튼의 그림자 효과는 clipToPadding = true일 경우 잘려 보일 수 있음.

            linearLayout {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER

                pickImageView(ui, 0) // 지저분하게 중복되는 코드를 제거하기 위해 이미지뷰를 그리는 코드를 별도 함수로 분리한 것.
                space().lparams(dip(8)) // LinearLayout 내의 요소들 사이에 공간을 추가할 수 있음. 여기에서는 4개의 이미지뷰 사이에 각각  8dp의 공간을 줌.
                pickImageView(ui, 1)
                space().lparams(dip(8))
                pickImageView(ui, 2)
                space().lparams(dip(8))
                pickImageView(ui, 3)
            }

            textView("상품명 및 설명") {
                topPadding = dip(40)
                textSize = 16f
                textColorResource = R.color.colorPrimary
            }

            textInputLayout {
                topPadding = dip(20)
                textInputEditText {
                    hint = "상품명"
                    setSingleLine()
                    bindString(ui.owner, viewModel.productName)

                    // textChangedListener{} 는 anko coroutines에서 제공하는 함수로 텍스트 뷰의
                    // 텍스트가 변경되었을 때 동작하는 이벤트 리스너이며 내부적으로는 텍스트뷰의
                    // addTextChangedListener()를 호출해주고 있음. addTextChangedListener()의
                    // 파라미터인 TextWatcher 인터페이스는 beforeTextChanged()와 onTextChanged()
                    // 그리고 afterTextChanged() 세 개의 콜백을 모두 구현해주어야 하지만 anko coroutines
                    // 에서 제공하는 textChangedListener {}는 이들 중 일부만 구현할 수 있는 편의성을
                    // 제공함. 여기에서는 onTextChanged 콜백을 정의해 뷰모델의 checkProductNameLength()
                    // 등을 호출해주게 만들었음. onTextChanged{}의 언더스코어(_)는 람다의 파라미터 변수를
                    // 사용하지 않을 때 간편하게 대체할 수 있는 기호.
                    textChangedListener {
                        onTextChanged { _, _, _, _ ->
                            viewModel.checkProductNameLength()
                        }
                    }
                }
                textView("0/40") {
                    leftPadding = dip(4)
                    textSize = 12f
                    textColorResource = R.color.colorPrimary
                    bindString(ui.owner, viewModel.productNameLength)
                }
            }

            textInputLayout {
                topPadding = dip(20)
                textInputEditText {
                    hint = "상품 설명"
                    maxLines = 6
                    bindString(ui.owner, viewModel.description)
                    textChangedListener {
                        onTextChanged { _, _, _, _ ->
                            viewModel.checkDescriptionLength()
                        }
                    }
                }
                textView("0/500") {
                    leftPadding = dip(4)
                    textSize = 12f
                    textColorResource = R.color.colorPrimary
                    bindString(ui.owner, viewModel.descriptionLength)
                }
            }

            textView("카테고리") {
                topPadding = dip(40)
                textSize = 16f
                textColorResource = R.color.colorPrimary
            }

            verticalLayout {
                topPadding = dip(12)
                bottomPadding = dip(12)
                backgroundColor = 0xEEEEEEEE.toInt()

                // 스피너는 드롭다운 UI를 통해 값 집합에서 하나의 값을 선택할 수 있는 방법을 제공.
                // 스피너의 배경을 설정하는 방법에는 여러 가지가 있지만 여기에서는 간단하게 배경색이
                // 들어간 verticalLayout으로 스피너를 감싸주었음.
                spinner {
                    // 스피너에 데이터 리스트를 표시해주려면 BaseAdapter 클래스를 상속받은 어댑터를
                    // 구현해주어야 하지만 간단하게는 ArrayAdapter를 사용해 문자열 리스트를 보여줌.
                    // bindStringEntries() 함수는 ArrayAdapter를 이용해 MutableLiveData<List<String>>
                    // 타입의 데이터를 스피너에 바인딩해줌.
                    bindStringEntries(ui.owner, viewModel.categories)

                    // onItemSelectedListener{} 는 스피너에서 아이템이 선택되었을 때 동작하는 콜백을
                    // 정의. 내부적으로는 setOnItemSelectedListener()를 사용하고 있음. setOnItemSelectedListener()
                    // 함수의 파라미터인 onItemSelectedListener 인터페이스는 onItemSelected()와 onNotingSelected()를
                    // 구현해주게 되어 있지만 anko coroutines에서 제공하는 onItemSelectedListener{} 는 이들 중 일부만
                    // 구현 할 수 있는 편의성을 제공합니다. 여기에서는 onItemSelected 콜백만 지정해 뷰모델의 onCategorySelect()
                    // 함수를 호출하도록 구현.
                    onItemSelectedListener {
                        onItemSelected { _, _, position, _ ->
                            viewModel.onCategorySelect(position)
                        }
                    }
                }
            }.lparams(matchParent) {
                topMargin = dip(20)
            }

            textView("판매 가격") {
                topPadding = dip(40)
                textSize = 16f
                textColorResource = R.color.colorPrimary
            }

            textInputLayout {
                topPadding = dip(20)
                textInputEditText {
                    hint = "Ex) 1000"
                    setSingleLine()
                    // inputType은 입력값의 타입을 설정함. 이 값을 InputType.TYPE_CLASS_NUMBER
                    // 로 설정하는 경우 소프트키보드가 숫자패드로 변경되며 입력 값은 숫자만 들어감.
                    inputType = InputType.TYPE_CLASS_NUMBER
                    bindString(ui.owner, viewModel.price)
                }
            }

            button("상품 등록") {
                backgroundColorResource = R.color.colorPrimary
                textColor = Color.WHITE
                onClick { viewModel.register() }
            }.lparams(matchParent, wrapContent) {
                topMargin = dip(40)
            }
        }
    }

    // imageView() 함수를 사용하기 위한 AnkoContext 객체와 이미지의 번호를 파라미터로 받고 있음.
    // 이 이미지 번호는 onClick 이벤트에서 ProductRegistrationViewModel의 pickImage() 함수를
    // 호출하는 파라미터로 쓰임.
    private fun _LinearLayout.pickImageView(
        ui: AnkoContext<ProductRegistrationActivity>,
        imageNum: Int
    ) = imageView(R.drawable.ic_baseline_search_24) {
        scaleType = ImageView.ScaleType.CENTER
        backgroundColor = 0xFFEEEEEE.toInt() // View에서 색상은 Int 값으로 표현. 하지만 대부분의 경우 색상을 16진수 값으로 표현하는 것이 가독성이 좋음. 16진수로 표현하는 경우 Alpha, Red, Green, Bule 값의 순서로 0xAARRGGBB와 같이 나타낼 수 있으며 결국은 Int 값이 필요하므로 toInt() 함수를 호출해서 Int 형으로 변환해주어야 함.

        onClick { viewModel.pickImage(imageNum) }
        // bindUrl() 함수는 AnkoMVVM의 데이터바인딩 함수로 이미지의 MutableLiveData<String>
        // 타입의 주소와 onUrlchange 콜백을 파라미터로 받음.  onUrlChange 콜백은 주소가 변경
        // 되었을 때 호출되며 변경된 주소를 파라미터로 전달하므로 이 때 Glide로 이미지를 변경해줄
        // 수 있음. 이미지 업로드 후 반환되는 경로는 도메인을 제외한 경로이므로 ApiGenerator의
        // HOST를 프리픽스로 붙여주어야 함.
        bindUrl(ui.owner, viewModel.imageUrls[imageNum]) {
            it?.let {
                scaleType = ImageView.ScaleType.CENTER_CROP
                Glide.with(this)
                    .load("${ApiGenerator.HOST}")
                    .centerCrop()
                    .into(this)
            }
        }
    }.lparams(dip(60), dip(60))
}

