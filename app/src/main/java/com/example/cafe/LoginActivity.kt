package com.example.cafe

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.cafe.databinding.ActivityLoginBinding
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

class LoginActivity:AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    private val callback: (OAuthToken?,Throwable?) -> Unit = {token, error ->
        if(error != null){
            //로그인 실패
        }else if(token != null){
            //로그인 성공
            Log.e("로그인액티비티","로그인 카카오 어카운드 토큰 : $token")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Kakao SDK 초기화
        KakaoSdk.init(this, "a680ab6aa00bc1775035020ab328f996")
        
        // 카카오톡 로그인 버튼 클릭 시
        binding.kakaoTalkLoginButton.setOnClickListener {

            // 카카오톡으로 로그인 가능시
            if(UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
                //카카오톡 로그인
                UserApiClient.instance.loginWithKakaoTalk(this){token, error ->

                    if(error != null){
                        //카카오톡 로그인 실패

                        //의도적으로 로그인을 그만둔거면
                        if(error is ClientError && error.reason == ClientErrorCause.Cancelled){
                            // 다시 카카오톡 로그인
                            return@loginWithKakaoTalk
                        }
                        // 의도적으로 로그인을 그만둔게 아니면 카카오계정으로 로그인
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                        // 카카오톡로그인이 에러가 없으면
                    }else if(token != null){
                        //로그인 성공
                        Log.e("로그인액티비티","토큰 == $token")
                    }

                }
            // 카카오톡 로그인 안되면 카카오 계정으로 로그인
            }else{
                //카카오계정 로그인
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }

        }
       
    }
}