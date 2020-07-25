package com.example.parayo.signup

import android.graphics.Typeface
import android.text.InputType
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import com.example.parayo.R
import net.codephobia.ankomvvm.databinding.bindString
import org.jetbrains.anko.*
import org.jetbrains.anko.design.textInputEditText
import org.jetbrains.anko.design.textInputLayout
import org.jetbrains.anko.sdk27.coroutines.onClick

class SignupActivityUI (
    private val viewModel: SignupViewModel // 1 SignupActivityUI는 SignupViewModel의 데이터에 의존적이기  때문에 생성자에서 SignupViewModel을 주입 받음.
) : AnkoComponent<SignupActivity> {

    override fun createView(ui: AnkoContext<SignupActivity>) =
        ui.linearLayout { // 2 ui.linearLayout {...}은 UI의 최상위 컨테이너로 LinearLayout을 생성해줌. 이 레이아웃은 세로 또는 가로의 단일 방향으로 모든 하위 항목을 정렬하는 뷰 그룹. 회원 가입 화면은 자식 요소들이 단순하게 세로로 배열되었기 때문에 LinearLayout을 사용.
            orientation = LinearLayout.VERTICAL // LinearLayout의 하위 항목들을 세로로 배열할 것을 나타냄.
            gravity = Gravity.CENTER_VERTICAL // LinearLayout의 높이가 자식 요소들이 차지하는 높이의 합보다 큰 경우에 한해 자식 요소들을 세로 중앙에 배치.
            padding = dip(20) // LinearLayout의 안쪽에 20DIP의 여백이 주어짐을 의미. dip(Int)는 Anko에서 제공하는 헬퍼 함수로, 정수를 안드로이드 화면에 적합한 DIP 단위로 변환해주는 함수.

            textView("회원가입") { // 3 단순한 문자열을 보여주는 TextView를 생성.
                textAlignment = TextView.TEXT_ALIGNMENT_CENTER // 텍스트의 가운데 정렬
                textSize = 20f // 텍스트의 폰트 20SP. 이 값은 float로 입력하게 되어 있음.
                typeface = Typeface.DEFAULT_BOLD // 텍스트를  굵은 글씨로 보여줌.
                textColorResource = R.color.colorPrimary // 텍스트의 색깔을 colors.xml 파일에 정의된 colorPrimary 값으로 설정해줌. R 클레스는 여러 라이브러리에도 동일한 이름으로 존재하기 때문에 여기에서는  import 구문이 com.example.parayo.R을 지시하고 있음을 명심.
            }.lparams(width = matchParent) { // 이 TextView의 레이아웃 파라미터들을 설정. width = matchParent로 TextView의 넓이를 부모(여기에서는 LinearLayout) 안에 가득 차도록 설정.
                bottomMargin = dip(50)  // 이 TextView 아래에 50DIP의 바깥쪽 여백을 추가함을 의미.
            }

            textInputLayout { // 4 textInputLayout과 textInputEditText는 항상 둘이 중첩되어 따라다닌다고 생각하는 것이 편함. editText를 사용할 수도 있지만 일반적인 경우  textInputEditText가 디자인적으로 더 우수하다고 판단해 이를 사용하게 되었음. 사용법은 editText와 유사하니 필요한 경우 editText로 바꿔사용해도 무방.
                textInputEditText {
                    hint = "Email" // 텍스트 입력란이 비어있을 때 임시로 보여줄 테스트.
                    setSingleLine() // 텍스트 입력란이 줄바꿈을 허용하지 않도록 설정.
                    bindString(ui.owner, viewModel.email)
                }
            }.lparams(width = matchParent) {
                bottomMargin = dip(20)
            }

            textInputLayout {
                textInputEditText {
                    hint = "Name"
                    setSingleLine()
                    bindString(ui.owner, viewModel.name)
                }
            }.lparams(width = matchParent) {
                bottomMargin = dip(20)
            }

            textInputLayout {
                textInputEditText {
                    hint = "Password"
                    setSingleLine()
                    inputType = InputType.TYPE_CLASS_TEXT or
                            InputType.TYPE_TEXT_VARIATION_PASSWORD // 비밀번호와 같은 필드에 사용하는 옵션으로, 이 값이 설정된 텍스트 입력란은 ****와 같이 읽을 수 없도록 마스킹 처리가 됨.
                    bindString(ui.owner, viewModel.password) // AnkoMVVM에서 제공하는 데이터 바인딩 함수. 이 함수는 텍스트 입력란과 SignupViewModel의 email 같은 데이터를 연결해 서로의 값을 동기화 하도록 도와줌. 이메일 입력란에 이메일을 입력했을 경우 SignupViewModel의 email 필드의 값이 입력한 이메일로 자동 반영되는 것.
                }
            }.lparams(width = matchParent) {
                bottomMargin = dip(20)
            }

            button("회원가입") { // 5 넓이가 부모 컨테이너에 가득찬 가장 단순한 형태의 버튼을 추가.
                onClick { viewModel.signup() } // 클릭 이벤트 리스너를 설정할 수 있음. 람다 블록을 넘기는 것으로 버튼을 클릭했을 때 실행되는 코드를 정의할 수 있음. 이 람다 블록은 코루틴으로 실행되기 때문에 viewModel.signup()은 suspend 함수로 선언될 수 있음.
            }.lparams(width = matchParent)
        }
}