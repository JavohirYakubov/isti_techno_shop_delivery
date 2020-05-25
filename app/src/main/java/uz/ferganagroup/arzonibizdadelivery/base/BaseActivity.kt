package uz.ferganagroup.arzonibizdadelivery.base

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import uz.ferganagroup.arzonibizdadelivery.R
import uz.ferganagroup.arzonibizdadelivery.utils.LocaleManager
import uz.ferganagroup.arzonibizdadelivery.utils.Prefs
import uz.ferganagroup.arzonibizdadelivery.view.custom.ProgressDialogFragment

abstract class BaseActivity : AppCompatActivity() {
    abstract fun getLayout(): Int
    abstract fun initViews()
    abstract fun loadData()
    abstract fun initData()
    abstract fun updateData()
    var progressDialogFragment: ProgressDialogFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        initViews()
        loadData()
        initData()
    }

    fun checkUser(): Boolean{
        return !Prefs.getToken().isNullOrEmpty()
    }

    open fun updateStore(){

    }

    fun hideFragments(){
        supportFragmentManager.fragments.forEach {
            if (it.isAdded && it.isVisible){
                supportFragmentManager.beginTransaction()
                    .hide(it)
                    .commitAllowingStateLoss()
            }
        }
    }

    fun setProgress(inProgress: Boolean){
        if (inProgress){
            progressDialogFragment = ProgressDialogFragment()
            progressDialogFragment?.isCancelable = false
            progressDialogFragment?.show(supportFragmentManager, progressDialogFragment!!.tag)
        }else{
            progressDialogFragment?.dismiss()
        }
    }

    fun popFragmentsWhile(tag: String) {
        supportFragmentManager.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }


    fun pushFragment(layoutId: Int, fragment: BaseFragment, tag: String) {
        hideKeyboard()
        supportFragmentManager
            .beginTransaction()
            .add(layoutId, fragment)
            .addToBackStack("")
            .commit()
        //Log.d("frag", tag)

    }

    fun pushFragmentWithoutHistory(layoutId: Int, fragment: BaseFragment, tag: String) {
        hideKeyboard()
        supportFragmentManager
            .beginTransaction()
            .add(layoutId, fragment)
//            .addToBackStack(tag)
            .commit()
        //Log.d("frag", tag)

    }

    fun pushBtmToTopFragment(layoutId: Int, fragment: BaseFragment, tag: String) {
        hideKeyboard()
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up)
            .add(layoutId, fragment)
            .addToBackStack("")
            .commit()

        //Log.d("frag", tag)
    }

    fun pushRightToLeftFragment(layoutId: Int, fragment: BaseFragment, tag: String) {
        hideKeyboard()
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
            .add(layoutId, fragment)
            .addToBackStack(tag)
            .commit()

        //Log.d("frag", tag)
    }

    fun pushFadeInToFadeOut(layoutId: Int, fragment: BaseFragment, tag: String) {
        hideKeyboard()
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            .add(layoutId, fragment)
            .addToBackStack(tag)
            .commit()

        //Log.d("frag", tag)
    }

    fun pushFragment(layoutId: Int, fragment: BaseFragment, tag: String, args: Bundle) {
        hideKeyboard()
        fragment.arguments = args
        supportFragmentManager
            .beginTransaction()
            .add(layoutId, fragment)
            .addToBackStack("")

            .commit()

        //Log.d("frag", tag)
    }

    fun pushFragment(
        layoutId: Int,
        fragment: BaseFragment,
        tag: String,
        args: Bundle,
        status: String
    ) {
        hideKeyboard()
        fragment.arguments = args
        supportFragmentManager
            .beginTransaction()
            .add(layoutId, fragment)
            .addToBackStack(tag)
            .commit()

        //Log.d("frag", tag)
    }

    fun hideKeyboard() {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it

        view?.clearFocus()
        view?.isSelected = false

        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleManager.setLocale(newBase))
    }
}