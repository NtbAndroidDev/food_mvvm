package project.ntb.food.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import project.ntb.food.R
import project.ntb.food.constants.AppConstants.Companion.CATEGORY_NAME
import project.ntb.food.constants.AppConstants.Companion.MEAL_AREA
import project.ntb.food.constants.AppConstants.Companion.MEAL_ID
import project.ntb.food.constants.AppConstants.Companion.MEAL_NAME
import project.ntb.food.constants.AppConstants.Companion.MEAL_STR
import project.ntb.food.constants.AppConstants.Companion.MEAL_THUMB
import project.ntb.food.ui.activites.MealDetailsActivity

class MealBottomDialog() : BottomSheetDialogFragment() {
    private var mealName = ""
    private var mealId =""
    private var mealImg = ""
    private var mealCountry = ""
    private var mealCategory = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
        val b = arguments
        mealName = b!!.getString(MEAL_NAME).toString()
        mealId =b!!.getString(MEAL_ID).toString()
        mealImg =b!!.getString(MEAL_THUMB).toString()
        mealCategory =b!!.getString(CATEGORY_NAME).toString()
        mealCountry =b!!.getString(MEAL_AREA).toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = LayoutInflater.from(context).inflate(R.layout.bottom_sheet_dialog, container, false)
        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prepareView(view)

        view.setOnClickListener {
            val intent = Intent(context, MealDetailsActivity::class.java)
            intent.putExtra(MEAL_ID,mealId)
            intent.putExtra(MEAL_STR,mealName)
            intent.putExtra(MEAL_THUMB,mealImg)
            startActivity(intent)
        }

    }

    fun prepareView(view:View){
        val tvMealName = view.findViewById<TextView>(R.id.tv_meal_name_in_btmsheet)
        val tvMealCategory = view.findViewById<TextView>(R.id.tv_meal_category)
        val tvMealCountry = view.findViewById<TextView>(R.id.tv_meal_country)
        val imgMeal = view.findViewById<ImageView>(R.id.img_category)

        Glide.with(view)
            .load(mealImg)
            .into(imgMeal)
        tvMealName.text = mealName
        tvMealCategory.text = mealCategory
        tvMealCountry.text = mealCountry
    }


}