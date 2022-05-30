package com.asl.lamdaclculator.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.Fragment
import com.asl.lamdaclculator.R
import com.asl.lamdaclculator.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {
    private lateinit var binding : ActivityMainBinding
    companion object {
        lateinit var replaceFragment: ((container: Int , fragment: Fragment , backStackTag: String?) -> Unit)
        lateinit var showDialog: ((dialog: AppCompatDialogFragment , tag: String) -> Unit)
        lateinit var clearBackStack: (() -> Unit)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply { setKeepOnScreenCondition { false } }
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment = { container , fragment , backStackTag ->
            replaceFragment(container , fragment , backStackTag)
        }
        showDialog = { dialog , tag ->
            dialog.show(supportFragmentManager , tag)
        }
        clearBackStack = {
            supportFragmentManager.popBackStack()
        }
    }

    private fun replaceFragment(
        container: Int ,
        fragment: Fragment ,
        backStackTag: String? = null
    ) {
        val transaction = supportFragmentManager.beginTransaction().setCustomAnimations(
            R.anim.enter_from_left ,
            R.anim.exit_to_right,
            R.anim.enter_from_right ,
            R.anim.exit_to_left
        ).replace(container , fragment)
        if (backStackTag != null) {
            transaction.addToBackStack(backStackTag)
        }
        transaction.commit()
    }
}