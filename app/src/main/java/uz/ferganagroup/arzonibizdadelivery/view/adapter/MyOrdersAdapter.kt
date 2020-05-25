package uz.ferganagroup.arzonibizdadelivery.view.adapter

import kotlinx.android.synthetic.main.my_order_item.view.*
import uz.ferganagroup.arzonibizdadelivery.R
import uz.ferganagroup.arzonibizdadelivery.base.formattedAmount
import uz.ferganagroup.arzonibizdadelivery.model.OrderModel
import uz.ferganagroup.arzonibizdadelivery.utils.DateUtils

interface MyOrdersAdapterListener{
    fun onClickStatus(item: OrderModel)
    fun onClickCall(item: OrderModel)
    fun onClickMap(item: OrderModel)
}
class MyOrdersAdapter(val list: List<OrderModel>, val handler: MyOrdersAdapterListener): BaseAdapter(list.toMutableList(), R.layout.my_order_item, null){
    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = list[position]

        holder.itemView.cardViewStatus.setOnClickListener {
            handler.onClickStatus(item)
        }

        holder.itemView.imgCall.setOnClickListener {
            handler.onClickCall(item)
        }

        holder.itemView.imgDirection.setOnClickListener {
            handler.onClickMap(item)
        }

        holder.itemView.tvId.text = "Заказ №${item.number}"
        holder.itemView.tvStoreName.text = item.skladname
        holder.itemView.tvStore.text = item.skladAddress
        holder.itemView.tvDeliveryAddress.text = item.deliveryAddress
        holder.itemView.tvSale.text = (-item.discount).toDouble().formattedAmount()
        holder.itemView.tvTotalAmount.text = (item.summa.toDouble() + item.deliverySumma.toDouble()).formattedAmount()
        holder.itemView.tvDeliveryAmount.text = item.deliverySumma.toDouble().formattedAmount()
        holder.itemView.tvClient.text = item.clientName

        when(item.cashbox){
            0 ->{
                holder.itemView.tvPaymentType.text = holder.itemView.context.getString(R.string.cashback)
            }
            1 ->{
                holder.itemView.tvPaymentType.text = holder.itemView.context.getString(R.string.terminal)
            }
            2 ->{
                holder.itemView.tvPaymentType.text = holder.itemView.context.getString(R.string.payme)
            }
            3 ->{
                holder.itemView.tvPaymentType.text = holder.itemView.context.getString(R.string.transfer)
            }
        }

        when(item.status){
            2->{
              holder.itemView.tvStatus.text = "В пути"
            }
            3->{
                holder.itemView.tvStatus.text = "Доставлен"
            }
        }
    }
}