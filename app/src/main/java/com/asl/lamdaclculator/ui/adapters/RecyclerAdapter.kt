package com.asl.lamdaclculator.ui.adapters

import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.asl.lamdaclculator.data.Equation
import com.asl.lamdaclculator.data.EquationHolder
import com.asl.lamdaclculator.databinding.RecyclerItemBinding

class RecyclerAdapter : RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {
    private var equationList = EquationHolder.equationList
    class ViewHolder(internal val binding : RecyclerItemBinding) : RecyclerView.ViewHolder(binding.root) {
        internal lateinit var equation : Equation
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myData : Equation = equationList[position]
        holder.equation = myData
        holder.binding.equation.text = myData.equation
        holder.binding.evaluation.text = myData.evaluation
        val value = TypedValue()
        when(position % 2){
            0 -> {
                holder.binding.root.context.theme.resolveAttribute(com.google.android.material.R.attr.colorSurface,value, true)
                holder.binding.itemHolder.setBackgroundColor(value.data)
            }
            1 -> {
                holder.binding.root.context.theme.resolveAttribute(com.google.android.material.R.attr.colorOnSecondary,value, true)
                holder.binding.itemHolder.setBackgroundColor(value.data)
            }
        }
    }

    override fun getItemCount(): Int {
        return EquationHolder.equationList.size
    }
}