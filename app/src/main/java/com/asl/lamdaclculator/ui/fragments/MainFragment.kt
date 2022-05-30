package com.asl.lamdaclculator.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.asl.lamdaclculator.R
import com.asl.lamdaclculator.data.Equation
import com.asl.lamdaclculator.data.EquationHolder
import com.asl.lamdaclculator.data.json.DataWriter
import com.asl.lamdaclculator.data.json.Json
import com.asl.lamdaclculator.databinding.FragmentMainBinding
import com.asl.lamdaclculator.ui.activities.MainActivity.Companion.replaceFragment
import com.asl.lamdaclculator.ui.activities.MainActivity.Companion.showDialog
import com.asl.lamdaclculator.ui.fragments.dialogs.ThemeDialog
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import net.objecthunter.exp4j.Expression
import net.objecthunter.exp4j.ExpressionBuilder

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var inputField: TextView
    private lateinit var resultField: TextView
    private val themeDialog = ThemeDialog()
    private var isEvaluated = false
    private var operatorList: MutableList<Int> = mutableListOf()
    private var decimalList: MutableList<Int> = mutableListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.theme -> {
                    showDialog(themeDialog, "themeDialog")
                    true
                }
                R.id.clear -> {
                    EquationHolder.equationList = mutableListOf()
                    DataWriter.writeFile( requireContext(), Json.toJson(mutableListOf()),"JSON_DATA")

                    true
                }
                else -> false
            }
        }

        binding.topAppBar.setNavigationOnClickListener {
            replaceFragment(R.id.fragment_container, HistoryFragment(), "History")
        }

        BottomSheetBehavior.from(binding.bottomSheet).apply {
            peekHeight = 100
            state = BottomSheetBehavior.STATE_COLLAPSED
        }
        inputField = binding.inputField
        inputField.text = "0"
        resultField = binding.resultField
        var isExpanded = false
        binding.bottomSheet.setOnClickListener{
            if (!isExpanded){
                isExpanded = true
                BottomSheetBehavior.from(binding.bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
            }
            else{
                isExpanded = false
                BottomSheetBehavior.from(binding.bottomSheet).state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        val numClickListener = View.OnClickListener { v ->
            if (endsWithSpecific("π")){
                addToList(operatorList)
                addInput("×")
            }
            if (inputField.text.length >= 2){
                if (endsWithOperator(inputField.text.substring(inputField.text.length-2 until inputField.text.length-1)) &&
                    endsWithSpecific("0")){
                    addToList(decimalList)
                    addInput(",")
                }
            }
            if(isStart()){
                removeInput()
            }
            addInput(v)
            isEvaluated = false
        }
        val operatorClickListener = View.OnClickListener { v ->
            v as Button
            if(endsWithNumber()||
                (v.text != "-" && endsWithSpecific("(")) ||
                (endsWithSpecific(")") && v.text != "-")){
                addToList(operatorList)
                addInput(v);addInput("(")
            }
            isEvaluated = false
        }
//        val decimalClickListener = View.OnClickListener {
//            val lastOperator = operatorList.maxOrNull() ?: 0
//            val lastDecimal = decimalList.maxOrNull() ?: -1
//            if (endsWithSpecific("π")){
//                addToList(operatorList);addInput("×")
//            }
//            if (lastDecimal < lastOperator){
//                addToList(decimalList); addInput(".")
//            }
//            Toast.makeText(requireActivity(), lastDecimal, Toast.LENGTH_SHORT).show()
//            isEvaluated = false
//        }
        val percentClickListener = View.OnClickListener {
            Snackbar.make(binding.root, "soon...", Snackbar.LENGTH_SHORT).show()
        }
        val piClickListener = View.OnClickListener { v ->
            if(endsWithNumber()){
                if (isStart()){
                    removeInput()
                }
                addInput(v)
            }
            else if (endsWithNumber()){
                addToList(operatorList)
                addInput("×π")
            }
            isEvaluated = false
        }
        val scientificClickListener = View.OnClickListener { v ->
            if (isStart()){
                removeInput()
            }
            addInput(v)
        }
        val clearClickListener = View.OnClickListener {
            for (i in arrayOf("sin(", "cos(", "tan(", "log(", "√(")) {
                if (inputField.text.endsWith(i)){
                    removeInput(i.length-1)
                }
            }
            if (inputField.text.length > 1){
                removeInput()
                decimalList.remove(inputField.text.length)
                operatorList.remove(inputField.text.length)
            }
            else{
                inputField.text = "0"
                resultField.text = ""
            }
            isEvaluated = false
        }
        val clearAllClickListener = View.OnClickListener {
            inputField.text = "0"
            resultField.text = ""
            operatorList = mutableListOf()
            decimalList = mutableListOf()
            isEvaluated = false
        }
        val evaluateClickListener = View.OnClickListener {
            if (!isEvaluated){
                val brCount = getCharCount('(') - getCharCount(')')
                addInput(")", brCount)
                val evaluation = inputField.text.toString()
                    .replace("×", "*")
                    .replace("÷", "/")
                    .replace("π", Math.PI.toString())
                    .replace(",", ".")
                    .replace("√", "sqrt")
                try {
                    val expression : Expression = ExpressionBuilder(evaluation).build()
                    resultField.text = expression.evaluate().toString().replace(".", ",")
                    if (resultField.text.endsWith(",0")){
                        resultField.text = resultField.text.dropLast(2)
                    }

                    val myEquation = Equation(inputField.text as String, resultField.text as String)
                    if (myEquation !in EquationHolder.equationList){
                        EquationHolder.equationList.add(myEquation)
                    }
                    DataWriter.writeFile(requireContext(), Json.toJson(EquationHolder.equationList),"JSON_DATA")
                    isEvaluated = true
                }
                catch (e : Exception){
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                    Log.d("EXCEPTION", "Message: ${e.message}")
                }

            }
            else{
                inputField.text = resultField.text.toString()
                resultField.text = ""
                operatorList = mutableListOf()
                decimalList = mutableListOf()
                if(getCharCount(',') >= 1){
                    addToList(decimalList, inputField.text.indexOf(","))
                }
                isEvaluated = true
            }
        }

        binding.buttonOne.setOnClickListener(numClickListener)
        binding.buttonTwo.setOnClickListener(numClickListener)
        binding.buttonThree.setOnClickListener(numClickListener)
        binding.buttonFour.setOnClickListener(numClickListener)
        binding.buttonFive.setOnClickListener(numClickListener)
        binding.buttonSix.setOnClickListener(numClickListener)
        binding.buttonSeven.setOnClickListener(numClickListener)
        binding.buttonEight.setOnClickListener(numClickListener)
        binding.buttonNine.setOnClickListener(numClickListener)
        binding.buttonZero.setOnClickListener(numClickListener)

        binding.buttonPlus.setOnClickListener(operatorClickListener)
        binding.buttonMinus.setOnClickListener(operatorClickListener)
        binding.buttonTimes.setOnClickListener(operatorClickListener)
        binding.buttonDivide.setOnClickListener(operatorClickListener)

        binding.buttonDecimal.setOnClickListener{
            val lastOperator = operatorList.maxOrNull() ?: 0
            val lastDecimal = decimalList.maxOrNull() ?: -1
            if (endsWithSpecific("π")){
                addToList(operatorList);addInput("×")
            }
            if (lastDecimal < lastOperator){
                addToList(decimalList); addInput(".")
            }
            Toast.makeText(requireActivity(), lastDecimal, Toast.LENGTH_SHORT).show()
            isEvaluated = false
        }
        binding.buttonPercent.setOnClickListener(percentClickListener)

        binding.buttonPi.setOnClickListener(piClickListener)

        binding.buttonBrOpen.setOnClickListener(numClickListener)
        binding.buttonBrClose.setOnClickListener(numClickListener)
        binding.buttonPow.setOnClickListener(numClickListener)

        binding.buttonRoot.setOnClickListener(scientificClickListener)
        binding.buttonSin.setOnClickListener(scientificClickListener)
        binding.buttonCos.setOnClickListener(scientificClickListener)
        binding.buttonTan.setOnClickListener(scientificClickListener)
        binding.buttonLog.setOnClickListener(scientificClickListener)

        binding.buttonClear.setOnClickListener(clearClickListener)
        binding.buttonClearAll.setOnClickListener(clearAllClickListener)
        binding.buttonEvaluate.setOnClickListener(evaluateClickListener)

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun addInput(str : String, repeat : Int = 0){
        inputField.text = inputField.text.toString() + str .repeat(repeat)
    }
    @SuppressLint("SetTextI18n")
    private fun addInput(btn : View, repeat : Int = 1){
        btn as Button
        inputField.text = inputField.text.toString() + btn.text.toString().repeat(repeat)
    }
    private fun removeInput(length : Int = 1){
        inputField.text = inputField.text.dropLast(length)
    }

    private fun addToList(list : MutableList<Int>, position: Int = inputField.text.length ){
        list.add(position)
    }

    private fun isStart() : Boolean{
        return inputField.text == "0"
    }
    private fun endsWithSpecific(str : String) : Boolean{
        return inputField.text.takeLast(str.length).endsWith(str)
    }
    private fun endsWithNumber() : Boolean{
        return inputField.text.takeLast(1) in arrayOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "π")
    }
    private fun endsWithOperator(str : String = inputField.text.toString()) : Boolean{
        return str.takeLast(1) in arrayOf("-", "+", "×", "÷", "(")
    }
    private fun getCharCount(ch : Char) : Int{
        return inputField.text.count { it == ch }
    }
    override fun onStop() {
        super.onStop()
        if (themeDialog.isStateSaved || themeDialog.isAdded || themeDialog.isVisible){
            themeDialog.dismissAllowingStateLoss()
        }
    }
}