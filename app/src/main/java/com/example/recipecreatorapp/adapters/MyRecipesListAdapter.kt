package com.example.recipecreatorapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.recipecreatorapp.R
import com.example.recipecreatorapp.fragments.RecipesFragment
import com.example.recipecreatorapp.models.Recipe
import com.example.recipecreatorapp.utils.Constants
import com.example.recipecreatorapp.utils.GlideLoader
import kotlinx.android.synthetic.main.item_list_layout.view.*

class MyRecipesListAdapter(private  val context: Context, private  var list:ArrayList<Recipe>, private val fragment: RecipesFragment): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_layout, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if(holder is MyViewHolder){
            GlideLoader(context).loadProductPicture(model.image, holder.itemView.iv_item_image)

            holder.itemView.tv_item_name.text = model.title
            holder.itemView.tv_item_category.text = model.category


            holder.itemView.ib_delete_product.setOnClickListener {
                fragment.deleteProduct(model.recipe_id)
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view:View): RecyclerView.ViewHolder(view)
}