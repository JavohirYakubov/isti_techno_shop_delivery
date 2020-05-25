package uz.ferganagroup.arzonibizdadelivery.screen.main

import android.app.Activity
import android.content.Intent
import android.view.MenuItem
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import q.rorbin.badgeview.QBadgeView
import uz.ferganagroup.arzonibizdadelivery.R
import uz.ferganagroup.arzonibizdadelivery.base.*
import uz.ferganagroup.arzonibizdadelivery.model.*
import uz.ferganagroup.arzonibizdadelivery.screen.main.orders.OrdersFragment
import uz.ferganagroup.arzonibizdadelivery.screen.main.myorders.MyOrdersFragment
import uz.ferganagroup.arzonibizdadelivery.screen.main.settings.SettingsFragment
import uz.ferganagroup.arzonibizdadelivery.utils.Constants
import uz.ferganagroup.arzonibizdadelivery.utils.Prefs

class MainActivity : BaseActivity(), SwipeRefreshLayout.OnRefreshListener {
    override fun getLayout(): Int = R.layout.activity_main
    lateinit var viewModel: MainViewModel

    var ordersFragment =
        OrdersFragment()
    var myOrdersFragment =
        MyOrdersFragment()
    var settingsFragment = SettingsFragment()

    override fun initViews() {
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this)
        }

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        pushFragment(R.id.container, ordersFragment, ordersFragment.tag ?: "")

        nav_bottom.setOnNavigationItemSelectedListener { item: MenuItem ->

            return@setOnNavigationItemSelectedListener when(item.itemId){
                R.id.navOrders -> {

                    if (ordersFragment.isAdded && ordersFragment.isVisible){

                    }else{
                        hideFragments()
                        if (!ordersFragment.isAdded){
                            supportFragmentManager.beginTransaction()
                                .add(R.id.container, ordersFragment)
                                .commitAllowingStateLoss()
                        }else{
                            supportFragmentManager.beginTransaction()
                                .show(ordersFragment)
                                .commitAllowingStateLoss()
                        }
                    }
                    true
                }
                R.id.navMyOrders -> {

                    if (myOrdersFragment.isAdded && myOrdersFragment.isVisible){

                    }else{
                        hideFragments()
                        if (!myOrdersFragment.isAdded){
                            supportFragmentManager.beginTransaction()
                                .add(R.id.container, myOrdersFragment)
                                .commitAllowingStateLoss()
                        }else{
                            supportFragmentManager.beginTransaction()
                                .show(myOrdersFragment)
                                .commitAllowingStateLoss()
                        }
                    }
                    true
                }
                R.id.navSettings -> {

                    if (settingsFragment.isAdded && settingsFragment.isVisible){

                    }else{
                        hideFragments()
                        if (!settingsFragment.isAdded){
                            supportFragmentManager.beginTransaction()
                                .add(R.id.container, settingsFragment)
                                .commitAllowingStateLoss()
                        }else{
                            supportFragmentManager.beginTransaction()
                                .show(settingsFragment)
                                .commitAllowingStateLoss()
                        }
                    }
                    true
                }
                else -> true
            }
        }

        if (intent.hasExtra(Constants.EXTRA_DATA_START_FRAGMENT)){
            nav_bottom.selectedItemId = intent.getIntExtra(Constants.EXTRA_DATA_START_FRAGMENT, R.id.navOrders)
        }
    }

    override fun updateStore() {
        super.updateStore()

    }

    override fun onBackPressed() {
        finish()
    }

    override fun loadData() {
        viewModel.getDeliveryInfo()
    }

    override fun initData() {

    }

    override fun updateData() {

    }

    override fun onRefresh() {

    }

    @Subscribe
    fun onEvent(event: EventModel<Int>){
        if (event.event == Constants.EVENT_UPDATE_BASKET){
            val menuView = nav_bottom.getChildAt(0) as BottomNavigationMenuView
            val target = menuView.getChildAt(2)
            QBadgeView(this).bindTarget(target).badgeNumber = event.data
        }

        loadData()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            MyOrdersFragment.REQUEST_CHECK_SETTINGS -> {
                if (resultCode == Activity.RESULT_OK) {
                    myOrdersFragment.getLastLocation()
                }
            }
        }
    }

}
