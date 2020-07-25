package com.example.parayo.product

import android.app.Application
import android.content.Intent
import com.example.parayo.product.registration.ProductRegistrationActivity
import net.codephobia.ankomvvm.lifecycle.BaseViewModel
class ProductMainViewModel(app: Application) : BaseViewModel(app) {

    fun openRegistrationActivity() {
        startActivity<ProductRegistrationActivity> {
            // 이 인텐트가 어떻게 핸들링될지를 지정. FLAG_ACTIVITY_SINGLE_TOP을
            // 지정하는 경우 이미 액티비티가 실행 중인 경우 액티비티가 두 번 뜨는 것을
            // 방지하기 위한 플래그로 사용.
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        }
    }
}