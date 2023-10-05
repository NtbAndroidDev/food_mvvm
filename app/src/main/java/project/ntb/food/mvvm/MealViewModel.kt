package project.ntb.food.mvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import project.ntb.food.data.pojo.Meal
import project.ntb.food.data.pojo.MealsResponse
import project.ntb.food.data.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MealViewModel(
//    val mealDatabase : MealsDatabase
) : ViewModel() {
    private var mutableMeal = MutableLiveData<List<Meal>>()

    fun getMealsByCategory(category:String){
        RetrofitInstance.foodApi.getMealsByCategory(category).enqueue(object :
            Callback<MealsResponse> {
            override fun onResponse(call: Call<MealsResponse>, response: Response<MealsResponse>) {
                mutableMeal.value = response.body()!!.meals
            }

            override fun onFailure(call: Call<MealsResponse>, t: Throwable) {
                Log.d(TAG,t.message.toString())
            }

        })
    }

    fun observeMeal(): LiveData<List<Meal>> {
        return mutableMeal
    }

//    fun insertMeal(meal : Meal){
//        viewModelScope.launch {
//            mealDatabase.dao().insertFavorite(meal)
//        }
//    }
}