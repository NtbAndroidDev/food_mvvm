package project.ntb.food.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import project.ntb.food.constants.AppConstants.Companion.CATEGORY_NAME
import project.ntb.food.data.pojo.Category
import project.ntb.food.databinding.FragmentCategoryBinding
import project.ntb.food.mvvm.CategoryViewModel
import project.ntb.food.ui.activites.MealActivity
import project.ntb.food.ui.adapters.CategoriesRecyclerAdapter

class CategoryFragment : Fragment() {
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var myAdapter: CategoriesRecyclerAdapter
    private lateinit var categoryMvvm:CategoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCategoryBinding.inflate(layoutInflater)
        myAdapter = CategoriesRecyclerAdapter()
        categoryMvvm = ViewModelProviders.of(this)[CategoryViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecyclerView()
        observeCategories()
        onCategoryClick()
    }
    private fun onCategoryClick() {
        myAdapter.onItemClicked(object : CategoriesRecyclerAdapter.OnItemCategoryClicked{
            override fun onClickListener(category: Category) {
                val intent = Intent(context, MealActivity::class.java)
                intent.putExtra(CATEGORY_NAME,category.strCategory)
                startActivity(intent)
            }
        })
    }

    private fun observeCategories() {
        categoryMvvm.observeCategories().observe(viewLifecycleOwner
        ) { value -> myAdapter.setCategoryList(value) }
    }

    private fun prepareRecyclerView() {
        binding.favoriteRecyclerView.apply {
            adapter = myAdapter
            layoutManager = GridLayoutManager(context,3, GridLayoutManager.VERTICAL,false)
        }
    }

}