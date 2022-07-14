package com.example.recipecreatorapp.activities

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.example.recipecreatorapp.R
import com.example.recipecreatorapp.firestore.FirestoreClass
import com.example.recipecreatorapp.models.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        tv_login_sign_up.setOnClickListener {
            onBackPressed()

        }

        btn_sign_up.setOnClickListener {
            registerUser()
        }

        setUpActionBar()
    }

    private fun setUpActionBar(){
        setSupportActionBar(toolbar_sign_up_activity)
    }

    private fun registerUser(){

        if(validateRegisterDetails()){

            showProgressDialog(resources.getString(R.string.please_wait))

            val email:String = et_email_sign_up.text.toString().trim{it <= ' '}
            val password:String = et_password_sign_up.text.toString().trim{it <= ' '}

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener( OnCompleteListener<AuthResult>{ task ->

                if (task.isSuccessful) {
                    val firebaseUser: FirebaseUser = task.result!!.user!!

                    val user = User(
                        firebaseUser.uid,
                        et_name.text.toString().trim{ it <= ' '},
                        et_email_sign_up.text.toString().trim{ it <= ' '}
                    )

                    FirestoreClass().registerUser(this, user)


                }else{
                    hideProgressDialog()
                    showErrorSnackBar(task.exception!!.message.toString(), true)
                }
            })
        }
    }

    private fun validateRegisterDetails(): Boolean{
        return when {
            TextUtils.isEmpty(et_name.text.toString().trim(){ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_enter_name), true)
                false
            }

            TextUtils.isEmpty(et_email_sign_up.text.toString().trim(){ it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }

            TextUtils.isEmpty(et_password_sign_up.text.toString().trim(){ it <= ' '})  -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            else ->{
                true
            }

        }
    }

    fun userRegistrationSuccess(){
        hideProgressDialog()
        Toast.makeText(this, resources.getString(R.string.register_success), Toast.LENGTH_SHORT).show()
        finish()
    }
}