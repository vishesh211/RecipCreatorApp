package com.example.recipecreatorapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.recipecreatorapp.R
import com.example.recipecreatorapp.activities.RecipeDetailsActivity
import com.example.recipecreatorapp.adapters.DashboardItemsListAdapter
import com.example.recipecreatorapp.databinding.FragmentDashboardBinding
import com.example.recipecreatorapp.firestore.FirestoreClass
import com.example.recipecreatorapp.models.Recipe
import com.example.recipecreatorapp.utils.Constants
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : BaseFragment() {
    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()

        getDashboardItemsList()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getDashboardItemsList(){
        showProgressDialog(resources.getString(R.string.please_wait))

        FirestoreClass().getDashboardItemsList(this)
    }

    fun successDashboardItemsList(dashboardItemsList: ArrayList<Recipe>){
        hideProgressDialog()

        if (dashboardItemsList.size >0){
            rv_dashboard_items.visibility = View.VISIBLE
            tv_no_dashboard_items_found.visibility = View.GONE

            rv_dashboard_items.layoutManager = LinearLayoutManager(activity)
            rv_dashboard_items.setHasFixedSize(true)

            val adapter = DashboardItemsListAdapter(requireActivity(), dashboardItemsList)
            rv_dashboard_items.adapter = adapter

//            adapter.setOnClickListener(object :
//                DashboardItemsListAdapter.OnClickListener {
//                override fun onClick(position: Int, recipe: Recipe) {
//
//                    val intent = Intent(context, RecipeDetailsActivity::class.java)
//                    intent.putExtra(Constants.EXTRA_RECIPE_ID, recipe.recipe_id)
//                    intent.putExtra(Constants.EXTRA_RECIPE_ID, recipe.user_id)
//                    startActivity(intent)
//                }
//            })


        }else {
            rv_dashboard_items.visibility = View.GONE
            tv_no_dashboard_items_found.visibility = View.VISIBLE
        }
    }

}