package project.ntb.food.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import project.ntb.food.R
import project.ntb.food.constants.AppConstants.Companion.CATEGORY_NAME
import project.ntb.food.constants.AppConstants.Companion.MEAL_ID
import project.ntb.food.constants.AppConstants.Companion.MEAL_STR
import project.ntb.food.constants.AppConstants.Companion.MEAL_THUMB
import project.ntb.food.data.pojo.Category
import project.ntb.food.data.pojo.Meal
import project.ntb.food.data.pojo.RandomMealResponse
import project.ntb.food.databinding.FragmentHomeBinding
import project.ntb.food.mvvm.HomeMVVM
import project.ntb.food.ui.activites.MealActivity
import project.ntb.food.ui.activites.MealDetailsActivity
import project.ntb.food.ui.adapters.CategoriesRecyclerAdapter
import project.ntb.food.ui.adapters.MostPopularRecyclerAdapter
import project.ntb.food.ui.adapters.OnItemClick
import project.ntb.food.ui.adapters.OnLongItemClick

class HomeFragment : Fragment(), CategoriesRecyclerAdapter.OnItemCategoryClicked, CategoriesRecyclerAdapter.OnLongCategoryClick, OnItemClick ,
    OnLongItemClick {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeMvvm : HomeMVVM
    private lateinit var meal: RandomMealResponse
    private lateinit var myAdapter: CategoriesRecyclerAdapter
    private lateinit var mostPopularFoodAdapter: MostPopularRecyclerAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentHomeBinding.inflate(layoutInflater)
        homeMvvm = ViewModelProviders.of(this)[HomeMVVM::class.java]
        myAdapter = CategoriesRecyclerAdapter()
        mostPopularFoodAdapter = MostPopularRecyclerAdapter()

        myAdapter.setOnLongCategoryClick(this)
        myAdapter.onItemClicked(this)
        mostPopularFoodAdapter.setOnClickListener(this)
        mostPopularFoodAdapter.setOnLongCLickListener(this)

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

        prepareCategoryRecyclerView()
        preparePopularMeals()

        observerRandomMeal()
        observerPopularFood()
        observerCategory()

        onRandomMealClick()

        // on search icon click
//        binding.imgSearch.setOnClickListener {
//            findNavController().navigate(R.id.action_homeFragment_to_searchFragment)
//        }

    }

    private fun onRandomMealClick() {
        binding.randomMeal.setOnClickListener {
            val temp = meal.meals[0]
            val intent = Intent(activity, MealDetailsActivity::class.java)
            intent.putExtra(MEAL_ID, temp.idMeal)
            intent.putExtra(MEAL_STR, temp.strMeal)
            intent.putExtra(MEAL_THUMB, temp.strMealThumb)
            startActivity(intent)
        }

    }



    private fun setMealsByCategoryAdapter(meals: List<Meal>) {
        mostPopularFoodAdapter.setMealList(meals)
        mostPopularFoodAdapter.notifyDataSetChanged()
    }
    private fun setCategoryAdapter(categories: List<Category>) {
        myAdapter.setCategoryList(categories)
        myAdapter.notifyDataSetChanged()
    }

    private fun prepareCategoryRecyclerView() {
        binding.recyclerView.apply {
            adapter = myAdapter
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
        }
    }

    private fun preparePopularMeals() {
        binding.recViewMealsPopular.apply {
            adapter = mostPopularFoodAdapter
            layoutManager = GridLayoutManager(context, 1, GridLayoutManager.HORIZONTAL, false)
        }
    }

    private fun cancelLoadingCase() {
        binding.apply {
            header.visibility = View.VISIBLE
            tvWouldLikeToEat.visibility = View.VISIBLE
            randomMeal.visibility = View.VISIBLE
            tvOverPupItems.visibility = View.VISIBLE
            recViewMealsPopular.visibility = View.VISIBLE
            tvCategory.visibility = View.VISIBLE
            categoryCard.visibility = View.VISIBLE
            loadingGif.visibility = View.INVISIBLE
            rootHome.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.white))

        }
    }



    private fun observerRandomMeal(){
        homeMvvm.observeRandomMeal().observe(viewLifecycleOwner) { value ->
            val imageUrl = value.meals[0].strMealThumb
            Glide.with(this@HomeFragment)
                .load(imageUrl)
                .into(binding.imgRandomMeal)
            meal = value
        }
    }

    private fun observerPopularFood(){
        homeMvvm.observeMealByCategory().observe(viewLifecycleOwner
        ) { value ->
            val meals = value!!.meals
            setMealsByCategoryAdapter(meals)
            cancelLoadingCase()
        }
    }

    private fun observerCategory(){
        homeMvvm.observeCategories().observe(viewLifecycleOwner
        ) {
            val categories = it!!.categories
            setCategoryAdapter(categories)

        }
    }

    override fun onClickListener(category: Category) {
        val intent = Intent(activity, MealActivity::class.java)
        intent.putExtra(CATEGORY_NAME, category.strCategory)
        startActivity(intent)
    }

    override fun onCategoryLongCLick(category: Category) {
        
    }

    override fun onItemClick(meal: Meal) {
        val intent = Intent(activity, MealDetailsActivity::class.java)
        intent.putExtra(MEAL_ID, meal.idMeal)
        intent.putExtra(MEAL_STR, meal.strMeal)
        intent.putExtra(MEAL_THUMB, meal.strMealThumb)
        startActivity(intent)
    }

    override fun onItemLongClick(meal: Meal) {
        homeMvvm.getMealByIdBottomSheet(meal.idMeal)
    }


}