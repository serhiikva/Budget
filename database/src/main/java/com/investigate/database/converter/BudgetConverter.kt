package com.investigate.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.investigate.database.entity.BudgetCategoryEntity

class BudgetConverter {

    private val gson = Gson()

    @TypeConverter
    fun fromBudgetCategoryList(value: List<BudgetCategoryEntity>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toBudgetCategoryList(value: String): List<BudgetCategoryEntity> {
        val type = object : TypeToken<List<BudgetCategoryEntity>>() {}.type
        return gson.fromJson(value, type)
    }

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val type = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, type)
    }
}