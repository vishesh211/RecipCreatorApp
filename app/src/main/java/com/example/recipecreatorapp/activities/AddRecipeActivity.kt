package com.example.recipecreatorapp.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.recipecreatorapp.R
import com.example.recipecreatorapp.firestore.FirestoreClass
import com.example.recipecreatorapp.models.Recipe
import com.example.recipecreatorapp.utils.Constants
import com.example.recipecreatorapp.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_add_recipe.*
import java.io.IOException

class AddRecipeActivity : BaseActivity(),View.OnClickListener {

    private var mSelectedImageFileURI: Uri? = null
    private var mRecipeImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_recipe)
        setupActionBar()

        iv_add_update_recipe.setOnClickListener(this)
        btn_submit_add_recipe.setOnClickListener(this)
    }

    private fun setupActionBar(){

        setSupportActionBar(toolbar_add_recipe_activity)

        val actionBar = supportActionBar
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_add_recipe_activity.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onClick(v: View?) {
        if(v != null){
            when (v.id){
                R.id.iv_add_update_recipe ->{
                    if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                        Constants.showImageChooser(this)
                    }else{
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), Constants.READ_STORAGE_PERMISSION_CODE)
                    }
                }

                R.id.btn_submit_add_recipe ->{
                    if (validateProductDetails()){
                        uploadProductImage()
                    }
                }
            }
        }
    }

    private fun uploadProductImage(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().uploadImageToCloudStorage(this, mSelectedImageFileURI, Constants.RECIPE_IMAGE)
    }

    fun imageUploadSuccess(imageURL: String){

        mRecipeImageURL = imageURL
        uploadRecipeDetails()
    }

    private fun uploadRecipeDetails(){
        val username = this.getSharedPreferences(Constants.RECIPE_CREATOR_PREFERENCES, Context.MODE_PRIVATE).getString(Constants.LOGGED_IN_USERNAME, "")

        val product = Recipe(
            FirestoreClass().getCurrentUserID(),
            username!!,
            et_recipe_title.text.toString().trim { it <= ' ' },
            et_recipe_category.text.toString().trim { it <= ' ' },
            et_recipe_description.text.toString().trim { it <= ' '},
            mRecipeImageURL
        )

        FirestoreClass().uploadRecipeDetails(this, product)

    }

    fun recipeUploadSuccess(){
        hideProgressDialog()
        Toast.makeText(this, resources.getString(R.string.recipe_uploaded_success_message), Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Constants.showImageChooser(this)
            }else{
                Toast.makeText(this, resources.getString(R.string.read_storage_permission_denied),Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE){
                if (data != null){
                    iv_add_update_recipe.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_vector_edit))
                    mSelectedImageFileURI = data.data
                    try {
                        GlideLoader(this).loadUserPicture(mSelectedImageFileURI!!, iv_recipe_image)
                    }catch (e: IOException){
                        e.printStackTrace()
                    }
                }
            }
        }else if (resultCode == Activity.RESULT_CANCELED){
            Log.e("Request Cancelled", "Image Selection Cancelled")
        }
    }

    private fun validateProductDetails(): Boolean {
        return when {

            mSelectedImageFileURI == null ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_select_recipe_image), true)
                false
            }


            TextUtils.isEmpty(et_recipe_title.text.toString().trim { it <= ' ' }) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_recipe_title), true)
                false
            }

            TextUtils.isEmpty(et_recipe_category.text.toString().trim { it <= ' ' }) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_recipe_category), true)
                false
            }

            TextUtils.isEmpty(et_recipe_description.text.toString().trim { it <= ' ' }) ->{
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_recipe_description), true)
                false
            }

            else ->{
                true
            }

        }
    }
}