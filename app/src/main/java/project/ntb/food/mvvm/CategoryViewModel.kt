package project.ntb.food.mvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import project.ntb.food.data.pojo.Category
import project.ntb.food.data.pojo.CategoryResponse
import project.ntb.food.data.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryViewModel : ViewModel(){
    private var categories: MutableLiveData<List<Category>> = MutableLiveData<List<Category>>()

    init {
        getCategories()
    }
    private fun getCategories(){
        RetrofitInstance.foodApi.getCategories().enqueue(object : Callback<CategoryResponse> {
            override fun onResponse(call: Call<CategoryResponse>, response: Response<CategoryResponse>) {
                categories.value = response.body()!!.categories
            }

            override fun onFailure(call: Call<CategoryResponse>, t: Throwable) {
                Log.d(TAG,t.message.toString())
            }

        })
    }

    fun observeCategories(): LiveData<List<Category>> {
        return categories
    }
}