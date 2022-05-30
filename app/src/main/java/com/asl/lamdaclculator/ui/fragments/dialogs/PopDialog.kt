package com.asl.lamdaclculator.ui.fragments.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import com.asl.lamdaclculator.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

// abstract class for later use
abstract class PopDialog : AppCompatDialogFragment(){

    protected open lateinit var title : String
    protected open lateinit var choiceArray : Array<String>
    abstract var config : Int
    abstract var stateArray: Array<Int>
    abstract var checkedItem : Int

    // abstract positive button function
    abstract fun positiveListener(checkedItem : Int)

    // returns dialog
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setSingleChoiceItems(choiceArray, checkedItem) { _, i -> checkedItem = i }
            .setPositiveButton(getString(R.string.ok)){ _, _ -> positiveListener(checkedItem) }
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
        return dialog.create()
    }
}