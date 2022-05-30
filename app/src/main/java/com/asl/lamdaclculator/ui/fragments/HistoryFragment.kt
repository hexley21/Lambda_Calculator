package com.asl.lamdaclculator.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.asl.lamdaclculator.data.EquationHolder
import com.asl.lamdaclculator.data.json.DataWriter
import com.asl.lamdaclculator.data.json.Json
import com.asl.lamdaclculator.databinding.FragmentHistoryBinding
import com.asl.lamdaclculator.ui.activities.MainActivity.Companion.clearBackStack
import com.asl.lamdaclculator.ui.adapters.RecyclerAdapter

class HistoryFragment : Fragment() {
    private lateinit var binding : FragmentHistoryBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)

        binding.topAppBar.setNavigationOnClickListener { clearBackStack() }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        EquationHolder.equationList = Json.toObject(DataWriter.readFile(requireContext(),"JSON_DATA"))
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = RecyclerAdapter()
    }
}