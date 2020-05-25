package uz.ferganagroup.arzonibizdadelivery.screen.splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.blankj.utilcode.util.NetworkUtils
import com.google.android.material.bottomsheet.BottomSheetDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.bottomsheet_language.view.*
import kotlinx.android.synthetic.main.must_update_layout.view.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import uz.ferganagroup.arzonibizdadelivery.App
import uz.ferganagroup.arzonibizdadelivery.BuildConfig
import uz.ferganagroup.arzonibizdadelivery.R
import uz.ferganagroup.arzonibizdadelivery.api.Client
import uz.ferganagroup.arzonibizdadelivery.api.ISTICheckModel
import uz.ferganagroup.arzonibizdadelivery.api.ISTIService
import uz.ferganagroup.arzonibizdadelivery.base.*
import uz.ferganagroup.arzonibizdadelivery.model.ISTIBaseResponse
import uz.ferganagroup.arzonibizdadelivery.screen.auth.SignActivity
import uz.ferganagroup.arzonibizdadelivery.screen.main.MainActivity
import uz.ferganagroup.arzonibizdadelivery.utils.LocaleManager
import uz.ferganagroup.arzonibizdadelivery.utils.Prefs

class SplashActivity : BaseActivity() {
    override fun getLayout(): Int = R.layout.activity_splash

    val compositeDisposable = CompositeDisposable()


    override fun initViews() {
        NetworkUtils.registerNetworkStatusChangedListener(object: NetworkUtils.OnNetworkStatusChangedListener{
            override fun onConnected(networkType: NetworkUtils.NetworkType?) {
                loadData()
            }

            override fun onDisconnected() {

            }
        })
    }

    override fun loadData() {
        NetworkUtils.isAvailableAsync {
            runOnUiThread {
                if (it){
                    getData()
                }else{
                    showWarning("Tarmoq bilan ulanish yo'q.")
                }
            }
        }
    }

    override fun initData() {

    }

    fun getData(){
        compositeDisposable.add(createService().getConfig().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<ISTIBaseResponse<ISTICheckModel>>(){
                override fun onComplete() {

                }

                override fun onNext(t: ISTIBaseResponse<ISTICheckModel>) {
                    //http://91.196.77.110:60020/MobilTest/odata/standard.odata/
                    if (!t.error){
                        val host = "http://" + t.items.ipaddress + ":" + t.items.ipport + "/" + t.items.href_address + "/"
                        App.imageBaseUrl = "http://" + t.items.ipaddress + ":" + t.items.ipport + "/img/" + t.items.secret_name + "/"
                        Client.initClient(App.app, host)
                        if ((t.items.delivery_version_code?.toIntOrNull() ?: 0) > BuildConfig.VERSION_CODE){
                            showMustUpdate()
                        }else if (Prefs.getToken().isNullOrEmpty()){
                            startClearTopActivity<SignActivity>()
                        }else{
                            startClearTopActivity<MainActivity>()
                            finish()
                        }
                    }else{
                        showError(t.message)
                    }
                }

                override fun onError(e: Throwable) {
                    showError(e.localizedMessage)
                }
            }))
    }

    override fun updateData() {

    }

    fun showMustUpdate(){
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.must_update_layout, null)
        bottomSheetDialog.setContentView(view)
        bottomSheetDialog.setCancelable(false)
        view.cardViewDownload.setOnClickListener {
            startActivityToOpenUrlInBrowser("https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}")
        }

        bottomSheetDialog.show()
    }


    fun showLanguageDialog(){
        val bottomSheetDialog = BottomSheetDialog(this)
        val viewLang = layoutInflater.inflate(R.layout.bottomsheet_language, null)
        bottomSheetDialog.setContentView(viewLang)
        viewLang.tvUzbCr.setOnClickListener {
            Prefs.setLang("uz")
            LocaleManager.setNewLocale(this, "uz")
            bottomSheetDialog?.dismiss()

            startClearTopActivity<SignActivity>()
        }
        viewLang.tvRu.setOnClickListener {
            Prefs.setLang("en")
            LocaleManager.setNewLocale(this, "en")
            bottomSheetDialog?.dismiss()

            startClearTopActivity<SignActivity>()
        }

        bottomSheetDialog.show()
    }

    companion object {
        fun createService(): ISTIService {
            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(
                    RxJava2CallAdapterFactory.create())
                .addConverterFactory(
                    GsonConverterFactory.create())
                .baseUrl("http://isti.uz/mobiles/")
                .build()

            return retrofit.create(ISTIService::class.java)
        }
    }
}
