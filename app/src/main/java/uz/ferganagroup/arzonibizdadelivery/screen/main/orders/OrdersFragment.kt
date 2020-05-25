package uz.ferganagroup.arzonibizdadelivery.screen.main.orders

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_orders.*
import org.greenrobot.eventbus.EventBus

import uz.ferganagroup.arzonibizdadelivery.R
import uz.ferganagroup.arzonibizdadelivery.base.BaseFragment
import uz.ferganagroup.arzonibizdadelivery.base.showError
import uz.ferganagroup.arzonibizdadelivery.base.showSuccess
import uz.ferganagroup.arzonibizdadelivery.model.EventModel
import uz.ferganagroup.arzonibizdadelivery.model.OrderModel
import uz.ferganagroup.arzonibizdadelivery.screen.main.MainViewModel
import uz.ferganagroup.arzonibizdadelivery.utils.Constants
import uz.ferganagroup.arzonibizdadelivery.view.adapter.OrdersAdapter
import uz.ferganagroup.arzonibizdadelivery.view.adapter.OrdersAdapterListener

/**
 * A simple [Fragment] subclass.
 */
class OrdersFragment : BaseFragment(), SwipeRefreshLayout.OnRefreshListener {
    override fun getLayout(): Int = R.layout.fragment_orders
    lateinit var viewModel: MainViewModel

    override fun setupViews() {
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        swipe.setOnRefreshListener(this)
        viewModel.progress.observe(this, Observer { inProgress ->
            swipe.isRefreshing = inProgress
        })

        viewModel.error.observe(this, Observer {
            activity?.showError(it)
        })

        viewModel.ordersData.observe(this, Observer {
            setData()
        })

        viewModel.updateStatusData.observe(this, Observer {
            activity?.showSuccess("Заказ был принят.")
            loadData()
            EventBus.getDefault().post(EventModel(Constants.EVENT_UPDATE_MY_ORDERS, 0))
        })
    }

    override fun loadData() {
        viewModel.getOrders()
    }

    override fun setData() {
        if (viewModel.ordersData.value != null){
            if (viewModel.ordersData.value!!.count() > 0){
                tvEmpty.visibility = View.GONE
            }else{
                tvEmpty.visibility = View.VISIBLE
            }
            recycler.layoutManager = LinearLayoutManager(activity)
            recycler.adapter = OrdersAdapter(viewModel.ordersData.value ?: emptyList(), object:
                OrdersAdapterListener {
                override fun onClickAccept(item: OrderModel) {
                    viewModel.acceptOrder(item.refKey)
                }

            })
        }else{
            tvEmpty.visibility = View.VISIBLE
        }
    }

    override fun onRefresh() {
        loadData()
    }

}
