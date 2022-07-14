package com.example.recipecreatorapp.firestore

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.recipecreatorapp.activities.AddRecipeActivity
import com.example.recipecreatorapp.activities.LoginActivity
import com.example.recipecreatorapp.activities.RecipeDetailsActivity
import com.example.recipecreatorapp.activities.SignUpActivity
import com.example.recipecreatorapp.fragments.DashboardFragment
import com.example.recipecreatorapp.fragments.RecipesFragment
import com.example.recipecreatorapp.models.Recipe
import com.example.recipecreatorapp.models.User
import com.example.recipecreatorapp.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FirestoreClass {

    private val mFirestore = FirebaseFirestore.getInstance()

    fun registerUser(activity:SignUpActivity, userInfo: User){

        mFirestore.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnCompleteListener {
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener {
                    e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while registering the user.", e)
            }
    }


    fun userDetails(activity: LoginActivity){
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                val user = document.toObject(User::class.java)!!

                val sharedPreferences = activity.getSharedPreferences(Constants.RECIPE_CREATOR_PREFERENCES, Context.MODE_PRIVATE)

                val editor: SharedPreferences.Editor = sharedPreferences.edit()

                editor.putString(Constants.LOGGED_IN_USERNAME, user.name)
                editor.apply()
                activity.userLoggedInSuccess(user)

            }
            .addOnFailureListener { e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while getting the details.",e)
            }
    }

    fun getRecipeDetails(activity: RecipeDetailsActivity, recipeId: String){
        mFirestore.collection(Constants.RECIPES)
            .document(recipeId)
            .get()
            .addOnSuccessListener { document ->
                val recipe = document.toObject(Recipe::class.java)

                if (recipe != null) {
                    activity.recipeDetailsSuccess(recipe)
                }

            }
            .addOnFailureListener {
                    e ->
                activity.hideProgressDialog()

                Log.e(activity.javaClass.simpleName, "Error while getting the recipe details.",e)
            }
    }

    fun getCurrentUserID():String{
        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""

        if(currentUser != null){
            currentUserID = currentUser.uid
        }

        return currentUserID

    }

    fun getDashboardItemsList(fragment: DashboardFragment){
        mFirestore.collection(Constants.RECIPES)
            .get()
            .addOnSuccessListener {
                    document ->
                Log.e(fragment.javaClass.simpleName, document.documents.toString())

                val recipesList: ArrayList<Recipe> = ArrayList()

                for (i in document.documents){
                    val recipe = i.toObject(Recipe::class.java)
                    recipe!!.recipe_id = i.id

                    recipesList.add(recipe)

                }

                fragment.successDashboardItemsList(recipesList)



            }
            .addOnFailureListener {
                    e ->
                fragment.hideProgressDialog()
                Log.e(fragment.javaClass.simpleName, "Error while getting dashboard items list.", e)
            }
    }

    fun deleteRecipe(fragment:RecipesFragment, recipeId:String){
        mFirestore.collection(Constants.RECIPES)
            .document(recipeId)
            .delete()
            .addOnSuccessListener {
                fragment.recipeDeleteSuccess()
            }
            .addOnFailureListener {
                    e->

                fragment.hideProgressDialog()
                Log.e(fragment.requireActivity().javaClass.simpleName, "Error while deleting the recipe.",e)
            }
    }

    fun getRecipesList(fragment: Fragment){
        mFirestore.collection(Constants.RECIPES)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e("Products List", document.documents.toString())

                val productList: ArrayList<Recipe> = ArrayList()

                for(i in document.documents){
                    val product = i.toObject(Recipe::class.java)

                    product!!.recipe_id = i.id

                    productList.add(product)
                }


                when(fragment){
                    is RecipesFragment ->{
                        fragment.successRecipesListFromFireStore(productList)
                    }
                }

            }
    }

    fun uploadImageToCloudStorage(activity: AddRecipeActivity, imageFileURI: Uri?, imageType:String){
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(imageType + System.currentTimeMillis() + "." + Constants.getFileExtension(activity, imageFileURI))

        sRef.putFile(imageFileURI!!)
            .addOnSuccessListener { taskSnapshot ->
                Log.e(
                    "Firebase Image URL",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
                )
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.e("Downloadable Image URL", uri.toString())
                        activity.imageUploadSuccess(uri.toString())
                    }
            }
            .addOnFailureListener { exception ->
                activity.hideProgressDialog()

                Log.e(
                    activity.javaClass.simpleName,
                    exception.message,
                    exception
                )
            }
    }

    fun uploadRecipeDetails(activity: AddRecipeActivity, recipeInfo: Recipe){
        mFirestore.collection(Constants.RECIPES)
            .document()
            .set(recipeInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.recipeUploadSuccess()
            }
            .addOnFailureListener { e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while uploading the Details.", e)
            }
    }
}