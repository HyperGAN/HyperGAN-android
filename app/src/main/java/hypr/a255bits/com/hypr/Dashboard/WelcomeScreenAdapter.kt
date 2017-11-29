package hypr.a255bits.com.hypr.Dashboard

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.pawegio.kandroid.alert
import hypr.a255bits.com.hypr.BuyGenerator
import hypr.a255bits.com.hypr.R
import kotlinx.android.synthetic.main.model_card.view.*
import org.greenrobot.eventbus.EventBus


class WelcomeScreenAdapter(val generators: MutableList<BuyGenerator>, val context: Context) : RecyclerView.Adapter<WelcomeScreenAdapter.CustomViewHolder>() {

    override fun getItemCount(): Int {
        return generators.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.model_card, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {
        holder?.title?.text = generators[position].name
        holder?.buyButton?.setOnClickListener {
            context.alert("Would you like to buy this model?", "Hypr") {
                positiveButton("Buy", { EventBus.getDefault().post(position) })
                negativeButton { dismiss() }
            }.show()
        }
    }

    class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.nameOfCard
        val buyButton: Button = view.buyButton

    }
}
