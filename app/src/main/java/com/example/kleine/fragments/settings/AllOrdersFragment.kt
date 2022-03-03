package com.example.kleine.fragments.settings

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kleine.R
import com.example.kleine.activities.ShoppingActivity
import com.example.kleine.adapters.recyclerview.AllOrdersAdapter
import com.example.kleine.databinding.FragmentAllOrdersBinding
import com.example.kleine.resource.Resource
import com.example.kleine.viewmodel.shopping.ShoppingViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class AllOrdersFragment : Fragment() {
    val TAG = "AllOrdersFragment"
    private lateinit var viewModel:ShoppingViewModel
    private lateinit var binding:FragmentAllOrdersBinding
    private lateinit var allOrdersAdapter:AllOrdersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = (activity as ShoppingActivity).viewModel
        viewModel.getUserOrders()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAllOrdersBinding.inflate(inflater)
        activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation)?.visibility = View.GONE
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeAllOrders()
        onCloseClick()
    }

    private fun onCloseClick() {
        binding.imgCloseOrders.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun observeAllOrders() {
        viewModel.userOrders.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resource.Loading -> {
                    showLoading()
                    return@observe
                }

                is Resource.Success -> {
                    hideLoading()
                    allOrdersAdapter.differ.submitList(response.data)
                    return@observe
                }

                is Resource.Error -> {
                    hideLoading()
                    Toast.makeText(
                        activity,
                        resources.getText(R.string.error_occurred),
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.e(TAG, response.message.toString())
                    return@observe
                }
            }
        }
    }

    private fun hideLoading() {
        binding.progressbarAllOrders.visibility = View.GONE

    }

    private fun showLoading() {
        binding.progressbarAllOrders.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        allOrdersAdapter = AllOrdersAdapter()
        binding.rvAllOrders.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = allOrdersAdapter
        }
    }
}