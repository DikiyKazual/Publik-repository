package com.example.logintest

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

//СДЕЛАЛ С ПОМОЩЬЮ ВИДЕО https://www.youtube.com/watch?v=q0jAFmB-wkU
//И СТАРНИЧКИ https://stackoverflow.com/questions/51736106/android-kotlin-facebook-login-get-user-data

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var loginButton: LoginButton
    private lateinit var circleImageView: CircleImageView
    private lateinit var txtEmail: TextView
    private lateinit var txtName: TextView

    private lateinit var callbackmanager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FacebookSdk.sdkInitialize(this)
        setContentView(R.layout.activity_main)

        loginButton = findViewById(R.id.login_button)
        circleImageView = findViewById(R.id.profile_pic)
        txtName = findViewById(R.id.profile_name)
        txtEmail = findViewById(R.id.profile_email)

        callbackmanager = CallbackManager.Factory.create()
        loginButton.setReadPermissions(Arrays.asList("email","public_profile"))

        LoginManager.getInstance().registerCallback(callbackmanager, object : FacebookCallback<LoginResult>{
            override fun onSuccess(loginResult: LoginResult) {
                val request = GraphRequest.newMeRequest(loginResult.accessToken) { `object`, _ ->
                    try {
                        val first_name: String = `object`.getString("first_name")
                        val last_name: String = `object`.getString("last_name")
                        val email: String = `object`.getString("email")
                        val id: String = `object`.getString("id")
                        val image_url: String = "https://graph.facebook.com/$id/picture?type=normal"
                        txtEmail.setText(email)
                        val nameString: String = first_name + " " +last_name
                        txtName.setText(nameString)
                        Glide.with(this@MainActivity).load(image_url).into(circleImageView);

                        if (`object`.has("id")) {
                        } else {
                        }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                val parameters = Bundle()
                parameters.putString("fields","first_name,last_name,email,id")
                request.parameters = parameters
                request.executeAsync()
            }

            override fun onCancel() {
                txtEmail.setText("")
                txtName.setText("")
                circleImageView.setImageResource(0);
            }

            override fun onError(exception: FacebookException) {
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackmanager.onActivityResult(requestCode, resultCode, data)
    }

    private fun checkLoginStatus(){
        if(AccessToken.getCurrentAccessToken()==null) {
            txtEmail.setText("")
            txtName.setText("")
            circleImageView.setImageResource(0);
        }
    }
}
