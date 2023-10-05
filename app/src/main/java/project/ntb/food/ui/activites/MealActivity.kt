package project.ntb.food.ui.activites

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import project.ntb.food.R
import project.ntb.food.constants.AppConstants.Companion.CATEGORY_NAME
import project.ntb.food.constants.AppConstants.Companion.MEAL_ID
import project.ntb.food.constants.AppConstants.Companion.MEAL_STR
import project.ntb.food.constants.AppConstants.Companion.MEAL_THUMB
import project.ntb.food.databinding.ActivityMealBinding
import project.ntb.food.ui.adapters.MealRecyclerAdapter
import project.ntb.food.ui.adapters.SetOnMealClickListener
import project.ntb.food.mvvm.MealViewModel
import project.ntb.food.data.pojo.Meal

class MealActivity : AppCompatActivity(), SetOnMealClickListener {
    private lateinit var binding : ActivityMealBinding
    private lateinit var mealActivityMvvm: MealViewModel
    private lateinit var myAdapter: MealRecyclerAdapter
    private var categoryNme = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealBinding.inflate(layoutInflater)
        mealActivityMvvm = ViewModelProviders.of(this)[MealViewModel::class.java]
        setContentView(binding.root)


        startLoading()
        prepareRecyclerView()
        mealActivityMvvm.getMealsByCategory(getCategory())


        mealActivityMvvm.observeMeal().observe(this
        ) { value ->
            value.let {
                value?.let {
                    myAdapter.setCategoryList(it)
                    binding.tvCategoryCount.text = categoryNme + " : " + value.size.toString()
                    hideLoading()
                }

            }
        }
    }

    private fun hideLoading() {
        binding.apply {
            loadingGifMeals.visibility = View.INVISIBLE
            mealRoot.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.white))
        }    }

    private fun startLoading() {
        binding.apply {
            loadingGifMeals.visibility = View.VISIBLE
            mealRoot.setBackgroundColor(ContextCompat.getColor(applicationContext,R.color.g_loading))
        }
    }

    private fun getCategory(): String {
        val tempIntent = intent
        val x = intent.getStringExtra(CATEGORY_NAME)!!
        categoryNme = x
        return x
    }

    private fun prepareRecyclerView() {
        myAdapter = MealRecyclerAdapter()
        myAdapter.setOnMealClickListener(this)
        binding.mealRecyclerview.apply {
            adapter = myAdapter
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }
    }

    override fun setOnClickListener(meal: Meal) {
        val intent = Intent(applicationContext, MealDetailsActivity::class.java)
        intent.putExtra(MEAL_ID, meal.idMeal)
        intent.putExtra(MEAL_STR, meal.strMeal)
        intent.putExtra(MEAL_THUMB, meal.strMealThumb)
        startActivity(intent)
    }
}
