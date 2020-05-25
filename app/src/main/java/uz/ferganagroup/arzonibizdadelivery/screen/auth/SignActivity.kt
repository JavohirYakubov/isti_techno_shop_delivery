package uz.ferganagroup.arzonibizdadelivery.screen.auth

import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.redmadrobot.inputmask.MaskedTextChangedListener
import cuz.ferganagroup.arzonibizdadelivery.services.sms.SmsReceiverService
import kotlinx.android.synthetic.main.activity_sign.*
import uz.ferganagroup.arzonibizdadelivery.R
import uz.ferganagroup.arzonibizdadelivery.base.*
import uz.ferganagroup.arzonibizdadelivery.model.DistrictModel
import uz.ferganagroup.arzonibizdadelivery.screen.main.MainActivity
import uz.ferganagroup.arzonibizdadelivery.utils.Constants
import uz.ferganagroup.arzonibizdadelivery.utils.Prefs

class SignActivity : BaseActivity() {
    override fun getLayout(): Int = R.layout.activity_sign

    lateinit var viewModel: SignViewModel

    override fun initViews() {
        viewModel = ViewModelProviders.of(this).get(SignViewModel::class.java)


        btnContinue.setOnClickListener {
            viewModel.login(edLogin.text.toString(), edPassword.text.toString())
        }

        viewModel.progress.observe(this, Observer {
            setProgress(it)
        })

        viewModel.error.observe(this, Observer {
            showError(it)
        })

        viewModel.loginResponse.observe(this, Observer {
            Prefs.setToken(it.token)
            Prefs.setClientInfo(it)
            startClearActivity<MainActivity>()
            finish()
        })

    }

    override fun loadData() {

    }

    override fun initData() {

    }

    override fun updateData() {

    }

}
