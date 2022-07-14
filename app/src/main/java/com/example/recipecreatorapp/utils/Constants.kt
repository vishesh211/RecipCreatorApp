package com.example.recipecreatorapp.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap

object Constants {
    const val RECIPES: String = "recipes"
    const val USERS: String = "users"
    const val EXTRA_RECIPE_ID = "extra_recipe_id"
    const val USER_ID = "user_id"
    const val PICK_IMAGE_REQUEST_CODE = 1
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val RECIPE_CREATOR_PREFERENCES: String = "RecipeCreatorPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val RECIPE_IMAGE = "Recipe_Image"

    fun showImageChooser(activity: Activity){
        val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }

    fun getFileExtension(activity: Activity, uri: Uri?): String? {
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(activity.contentResolver.getType(uri!!))

    }
}