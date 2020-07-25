package com.example.parayo.signup

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.parayo.api.ParayoApi
import com.example.parayo.api.request.SignupRequest
import com.example.parayo.api.response.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.codephobia.ankomvvm.lifecycle.BaseViewModel

import org.jetbrains.anko.error

class SignupViewModel(app: Application): BaseViewModel(app) {

    val email = MutableLiveData("")
    val name = MutableLiveData("")
    val password = MutableLiveData("")

    suspend fun signup() {
        val request = SignupRequest(email.value, password.value, name.value)
        if (isNotValidSignup(request))
            return

        try {
            val response = requestSignup(request)
            onSignupResponse(response)
        } catch(e: Exception) {
            error("signup error", e) // 1
            toast("알 수 없는 오류가 발생했습니다.") // 2
        }
    }

    // 3
    private fun isNotValidSignup(signupRequest: SignupRequest) =
        when {
            signupRequest.isNotValidEmail() -> {
                toast("이메일 형식이 정확하지 않습니다.")
                true
            }
            signupRequest.isNotValidPassword() -> {
                toast("비밀번호는 8자 이상 20자 이하로 입력해주세요.")
                true
            }
            signupRequest.isNotValidName() -> {
                toast("이름은 2자 이상 20자 이하로 입력해주세요.")
                true
            }
            else -> false
        }

    // 4
    private suspend fun requestSignup(request: SignupRequest) =
        withContext(Dispatchers.IO) {
            ParayoApi.instance.signup(request)
        }

    // 5
    private fun onSignupResponse(response: ApiResponse<Void>) {
        if (response.success) {
            toast("회원 가입이 되었습니다. 로그인 후 이용해주세요.")
            finishActivity() // 화면을 닫아주는 함수
        } else {
            toast(response.message ?: "알 수 없는 오류가 발생했습니다.")
        }
    }
}

// 1
// error(string, throwable) 함수는 AnkoLogger를 상속받은 클래스에서 사용할 수 있는 Anko 라이브러리의 함수.
// 이 함수는 기존까지의 Log.e(tag, message, throwable) 같은 지저분한 로깅 함수를 깔끄맣게 대체 해줌. 이 외에도
// AnkoLogger를 상속받으면 제공되는 로깅 함수는 info(string),  debug(string), wtf(string) 등 다양하게 준비되어 있음.
// BaseViewModel은 AnkoLogger를 상속받고 있으므로 SignupViewModel에서 별도의 상속 절차 없이 해당 함수들을 사용할 수 있음.
// 2
// toast(string) 또한 토스트 메시지를 띄울 때 쉽게 사용할 수 있도록 Anko에서 제공하는 헬퍼 함수를 ViewModel
// 에서도 동일하게 사용할 수 있도록 BaseViewModel에서 랩핑하고 있음.
// 3
// .isNotValidSignup() 함수는 요청 파라미터가 정확하게 입력되었는지를 검증해주는 함수. signup() 함수의 초반에 이 함수를
// 호출해 파라미터들이 올바르게 입력되지 않았다면 즉시 빠져나오도록 구현.
// 4
// requestSignup() 함수는 회원 가입 API를 호출해주는 코드. 네트워크 요청 시에는 언제든 오류가 발생할 가능성이 존재하므로
// 바깥쪽에서 try-catch로 묶어 오류메시지를 표시해줌. UI가 포함된 애플리케이션을 개발할 때에는 네트워크 요청이 일어나는 동안 UI가
// 멈춘 것처럼 보일 수 있기 때문에 네트워크 요청은 비동기로 실행하는 것이 중요. withContext 코루틴 빌더를 이용하면 현재 스레드를 블록킹
// 하지 않고 새로운 코루틴을 시작할 수 있음. 이 블록 내의 코드는 IO스레드에서 비동기로 실행되게 됨. 이 함수는 코루틴 내부에서 실행되거나
// suspend 함수 내부에서 실행되어야함 하기 때문에 requestSignup() 함수를 suspend로 선언해야 함. 또한 suspend 함수의 호출도
// 다른 코루틴 내부에서 일어나거나 또다른 suspend 함수 내에서 실행되어야 하기 때문에 signup() 함수도 suspend 함수로 정의.