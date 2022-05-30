package com.asl.lamdaclculator.data.json

import com.asl.lamdaclculator.data.Equation
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Json {
    companion object{
        fun toObject(string: String) : MutableList<Equation>{
            val typeToken = object : TypeToken<List<Equation>>() {}.type
            return Gson().fromJson(string,typeToken) ?: mutableListOf()
        }
        fun toJson(equations : MutableList<Equation>) : String{
            return Gson().toJson(equations)
        }
    }
}