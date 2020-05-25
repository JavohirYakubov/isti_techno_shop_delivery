package uz.ferganagroup.arzonibizdadelivery.view.adapter

import kotlinx.android.synthetic.main.order_item_layout.view.*
import uz.ferganagroup.arzonibizdadelivery.R
import uz.ferganagroup.arzonibizdadelivery.base.formattedAmount
import uz.ferganagroup.arzonibizdadelivery.base.loadImage
import uz.ferganagroup.arzonibizdadelivery.model.OrderModel
import uz.ferganagroup.arzonibizdadelivery.utils.DateUtils

interface OrdersAdapterListener{
    fun onClickAccept(item: OrderModel)
}
class OrdersAdapter(val list: List<OrderModel>, val handler: OrdersAdapterListener): BaseAdapter(list.toMutableList(), R.layout.order_item_layout, null){
    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = list[position]

        holder.itemView.cardViewAccept.setOnClickListener {
            handler.onClickAccept(item)
        }

//        holder.itemView.imgMap.loadImage("https://open.mapquestapi.com/staticmap/v4/getmap?key=6HvK8897wLRzaXeYNqhbAe15kcpmcY0t&size=150,100&type=map&imagetype=png&pois=${item.number},${item.lat},${item.long}&center=${item.lat},${item.long}&zoom=13")
        holder.itemView.tvDeliveryType.text = item.skladname
        holder.itemView.tvId.text = "Заказ №${item.number}"
        holder.itemView.tvStore.text = item.skladAddress
        holder.itemView.tvDeliveryAddress.text = item.deliveryAddress
    }
}