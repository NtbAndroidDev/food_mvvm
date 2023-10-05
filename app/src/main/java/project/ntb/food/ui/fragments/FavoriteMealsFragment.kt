package project.ntb.food.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import project.ntb.food.R
import project.ntb.food.constants.AppConstants.Companion.CATEGORY_NAME
import project.ntb.food.constants.AppConstants.Companion.MEAL_AREA
import project.ntb.food.constants.AppConstants.Companion.MEAL_ID
import project.ntb.food.constants.AppConstants.Companion.MEAL_NAME
import project.ntb.food.constants.AppConstants.Companion.MEAL_STR
import project.ntb.food.constants.AppConstants.Companion.MEAL_THUMB
import project.ntb.food.databinding.FragmentFavoriteMealsBinding
import project.ntb.food.ui.MealBottomDialog
import project.ntb.food.ui.activites.MealDetailsActivity
import project.ntb.food.ui.adapters.FavoriteMealsRecyclerAdapter
import project.ntb.food.mvvm.DetailsViewModel
import project.ntb.food.data.pojo.MealDB
import project.ntb.food.data.pojo.MealDetail


class FavoriteMealsFragment : Fragment() {
    private lateinit var binding: FragmentFavoriteMealsBinding
    private lateinit var myAdapter: FavoriteMealsRecyclerAdapter
    private lateinit var detailsMVVM: DetailsViewModel
    lateinit var recView: RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentFavoriteMealsBinding.inflate(layoutInflater)
        myAdapter = FavoriteMealsRecyclerAdapter()
        detailsMVVM = ViewModelProviders.of(this)[DetailsViewModel::class.java]

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
        prepareRecyclerView(view)
        onFavoriteMealClick()
        onFavoriteLongMealClick()
        observeBottomDialog()

        detailsMVVM.observeSaveMeal().observe(viewLifecycleOwner
        ) { value ->
            myAdapter.setFavoriteMealsList(value)
            if (value.isEmpty())
                binding.tvFavEmpty.visibility = View.VISIBLE
            else
                binding.tvFavEmpty.visibility = View.GONE
        }

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val favoriteMeal = myAdapter.getMelaByPosition(position)
                detailsMVVM.deleteMeal(favoriteMeal)
                showDeleteSnackBar(favoriteMeal)
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recView)
    }


    private fun observeBottomDialog() {
        detailsMVVM.observeMealBottomSheet().observe(viewLifecycleOwner,object : Observer<List<MealDetail>>{


            override fun onChanged(value: List<MealDetail>) {
                val bottomDialog = MealBottomDialog()
                val b = Bundle()
                b.putString(CATEGORY_NAME,value[0].strCategory)
                b.putString(MEAL_AREA,value[0].strArea)
                b.putString(MEAL_NAME,value[0].strMeal)
                b.putString(MEAL_THUMB,value[0].strMealThumb)
                b.putString(MEAL_ID,value[0].idMeal)
                bottomDialog.arguments = b
                bottomDialog.show(childFragmentManager,"Favorite bottom dialog")
            }

        })
    }

    private fun showDeleteSnackBar(favoriteMeal: MealDB) {
        Snackbar.make(requireView(),"Meal was deleted", Snackbar.LENGTH_LONG).apply {
            setAction("undo",View.OnClickListener {
                detailsMVVM.insertMeal(favoriteMeal)
            }).show()
        }
    }

    private fun prepareRecyclerView(v:View) {
        recView =v.findViewById<RecyclerView>(R.id.fav_rec_view)
        recView.adapter = myAdapter
        recView.layoutManager = GridLayoutManager(context,2, GridLayoutManager.VERTICAL,false)
    }

    private fun onFavoriteMealClick(){
        myAdapter.setOnFavoriteMealClickListener(object : FavoriteMealsRecyclerAdapter.OnFavoriteClickListener{
            override fun onFavoriteClick(meal: MealDB) {
                val intent = Intent(context, MealDetailsActivity::class.java)
                intent.putExtra(MEAL_ID,meal.mealId.toString())
                intent.putExtra(MEAL_STR,meal.mealName)
                intent.putExtra(MEAL_THUMB,meal.mealThumb)
                startActivity(intent)
            }

        })
    }

    private fun onFavoriteLongMealClick() {
        myAdapter.setOnFavoriteLongClickListener(object : FavoriteMealsRecyclerAdapter.OnFavoriteLongClickListener{
            override fun onFavoriteLongCLick(meal: MealDB) {
                detailsMVVM.getMealByIdBottomSheet(meal.mealId.toString())
            }

        })
    }


}