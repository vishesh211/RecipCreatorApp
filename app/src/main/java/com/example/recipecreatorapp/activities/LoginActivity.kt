package com.example.recipecreatorapp.activities

import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.example.recipecreatorapp.R
import com.example.recipecreatorapp.firestore.FirestoreClass
import com.example.recipecreatorapp.models.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

//        val user = FirebaseAuth.getInstance().currentUser
//        if (user != null) {
//            val i = Intent(this@LoginActivity, DashboardActivity::class.java)
//            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(i)
//        } else {
//            // User is signed out
//            Log.d(ContentValues.TAG, "onAuthStateChanged:signed_out")
//        }

        btn_login.setOnClickListener(this)
        tv_sign_up.setOnClickListener(this)

        setUpActionBar()
    }

    private fun setUpActionBar() {
        setSupportActionBar(toolbar_login_activity)
    }

    override fun onClick(view: View?) {
        if (view != null){
            when(view.id){
                R.id.btn_login ->{
                    logInRegisteredUser()
                }
                R.id.tv_sign_up ->{
                    val intent = Intent(this, SignUpActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun validateLoginDetails():Boolean{
        return when {
            TextUtils.isEmpty(et_email_login.text.toString().trim{it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(et_password_login.text.toString().trim{it <= ' '}) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun logInRegisteredUser(){
        if(validateLoginDetails()){
            showProgressDialog(resources.getString(R.string.please_wait))

            val email = et_email_login.text.toString().trim{ it <= ' '}
            val password = et_password_login.text.toString().trim{ it <= ' '}

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    task ->

                if(task.isSuccessful){

                    FirestoreClass().userDetails(this)
                }else{
                    hideProgressDialog()
                    showErrorSnackBar(task.exception!!.message.toString(), true)
                }
            }
        }
    }

    fun userLoggedInSuccess(user: User){
        hideProgressDialog()
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }
}