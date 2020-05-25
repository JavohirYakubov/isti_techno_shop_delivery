package uz.ferganagroup.arzonibizdadelivery.screen.main.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_settings.*

import uz.ferganagroup.arzonibizdadelivery.R
import uz.ferganagroup.arzonibizdadelivery.base.BaseFragment
import uz.ferganagroup.arzonibizdadelivery.base.startClearActivity
import uz.ferganagroup.arzonibizdadelivery.screen.splash.SplashActivity
import uz.ferganagroup.arzonibizdadelivery.utils.Prefs

/**
 * A simple [Fragment] subclass.
 */
class SettingsFragment : BaseFragment() {
    override fun getLayout(): Int = R.layout.fragment_settings

    override fun setupViews() {
        lyLogout.setOnClickListener {
            Prefs.clearAll()
            getBaseActivity {
                it.startClearActivity<SplashActivity>()
                it.finish()
            }
        }
        val delivery = Prefs.getDelivery()
        tvPersonName.text = delivery?.name
    }

    override fun loadData() {

    }

    override fun setData() {

    }

}
