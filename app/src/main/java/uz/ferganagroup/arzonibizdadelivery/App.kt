package uz.ferganagroup.arzonibizdadelivery

import android.app.Application
import android.util.Log
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.orhanobut.hawk.Hawk
import uz.ferganagroup.arzonibizdadelivery.services.sms.AppSignatureHelper
import uz.ferganagroup.arzonibizdadelivery.api.Client
import uz.ferganagroup.arzonibizdadelivery.api.ISTClient
import uz.ferganagroup.arzonibizdadelivery.utils.Prefs

class App : MultiDexApplication(){
    companion object{
        lateinit var app: App
        var imageBaseUrl = ""
    }
    override fun onCreate() {
        super.onCreate()
        app = this
        ISTClient.initClient(app)
        MultiDex.install(this)
        Prefs.init(this)
        Log.d("JW ISTI", AppSignatureHelper(this).appSignatures[0])
    }
}