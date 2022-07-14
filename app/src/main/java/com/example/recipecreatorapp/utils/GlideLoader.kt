package com.example.recipecreatorapp.utils

import android.content.Context
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.recipecreatorapp.R
import java.io.IOException

class GlideLoader(val context: Context) {

    fun loadUserPicture(image: Any, imageView: ImageView){
        try {
            Glide
                .with(context)
                .load(image)
                .centerCrop()
                .into(imageView);
        }catch (e:IOException){
            e.printStackTrace()

        }
    }

    fun loadProductPicture(image: Any, imageView: ImageView){
        try {
            Glide
                .with(context)
                .load(image)
                .centerCrop()
                .into(imageView);
        }catch (e:IOException){
            e.printStackTrace()

        }
    }
}