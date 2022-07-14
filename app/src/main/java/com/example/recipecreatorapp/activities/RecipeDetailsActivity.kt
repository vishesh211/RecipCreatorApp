package com.example.recipecreatorapp.activities

import android.os.Bundle
import com.example.recipecreatorapp.R
import com.example.recipecreatorapp.firestore.FirestoreClass
import com.example.recipecreatorapp.models.Recipe
import com.example.recipecreatorapp.utils.Constants
import com.example.recipecreatorapp.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_recipe_details.*

class RecipeDetailsActivity : BaseActivity() {
    private var mRecipeId: String = ""
    private lateinit var mRecipeDetails: Recipe

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipe_details)
        setUpActionBar()
        if(intent.hasExtra(Constants.EXTRA_RECIPE_ID)){
            mRecipeId = intent.getStringExtra(Constants.EXTRA_RECIPE_ID)!!
        }
        //getRecipeDetails()
    }

    private fun getRecipeDetails(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getRecipeDetails(this, mRecipeId)
    }

    fun recipeDetailsSuccess(recipe: Recipe) {

        mRecipeDetails = recipe
        GlideLoader(this).loadUserPicture(recipe.image, iv_recipe_detail_image)
        tv_recipe_details_title.text = recipe.title
        tv_recipe_details_description.text = recipe.recipeProcess
        tv_recipe_selected_category.text = recipe.category
        hideProgressDialog()
    }

    private fun setUpActionBar(){
        setSupportActionBar(toolbar_recipe_details_activity)

        val actionBar = supportActionBar
        if(actionBar!= null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_recipe_details_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}