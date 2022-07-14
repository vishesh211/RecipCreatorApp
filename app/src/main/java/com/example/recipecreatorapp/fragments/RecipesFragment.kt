package com.example.recipecreatorapp.fragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipecreatorapp.R
import com.example.recipecreatorapp.activities.AddRecipeActivity
import com.example.recipecreatorapp.adapters.MyRecipesListAdapter
import com.example.recipecreatorapp.databinding.FragmentRecipesBinding
import com.example.recipecreatorapp.firestore.FirestoreClass
import com.example.recipecreatorapp.models.Recipe
import kotlinx.android.synthetic.main.fragment_recipes.*


class RecipesFragment : BaseFragment() {
    private var _binding: FragmentRecipesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {

        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    fun deleteProduct(productID: String){
        showAlertDialogToDeleteProduct(productID)
    }

    fun recipeDeleteSuccess(){
        hideProgressDialog()

        Toast.makeText(
            requireActivity(),
            resources.getString(R.string.recipe_delete_success_message),
            Toast.LENGTH_SHORT
        ).show()


        getRecipesListFromFireStore()
    }

    private fun showAlertDialogToDeleteProduct(productID: String) {

        val builder = AlertDialog.Builder(requireActivity())
        builder.setTitle(resources.getString(R.string.delete_dialog_title))
        builder.setMessage(resources.getString(R.string.delete_dialog_message))
        builder.setIcon(android.R.drawable.ic_dialog_alert)
        builder.setPositiveButton(resources.getString(R.string.yes)) { dialogInterface, _ ->

            showProgressDialog(resources.getString(R.string.please_wait))
            FirestoreClass().deleteRecipe(this@RecipesFragment, productID)
            dialogInterface.dismiss()
        }


        builder.setNegativeButton(resources.getString(R.string.no)) { dialogInterface, _ ->

            dialogInterface.dismiss()
        }
        val alertDialog: AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }


    fun successRecipesListFromFireStore(productList: ArrayList<Recipe>){
        hideProgressDialog()

        if (productList.size >0){

            rv_my_recipe_items.visibility = View.VISIBLE
            tv_no_recipes_found.visibility = View.GONE

            rv_my_recipe_items.layoutManager = LinearLayoutManager(activity)
            rv_my_recipe_items.setHasFixedSize(true)
            val adapterProducts = MyRecipesListAdapter(requireActivity(), productList, this)

            rv_my_recipe_items.adapter = adapterProducts

        }else{
            rv_my_recipe_items.visibility = View.GONE
            tv_no_recipes_found.visibility = View.VISIBLE

        }

    }

    private fun getRecipesListFromFireStore(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FirestoreClass().getRecipesList(this)
    }

    override fun onResume() {
        super.onResume()
        getRecipesListFromFireStore()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentRecipesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_recipe_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add_product ->{
                startActivity(Intent(activity, AddRecipeActivity::class.java))
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

}