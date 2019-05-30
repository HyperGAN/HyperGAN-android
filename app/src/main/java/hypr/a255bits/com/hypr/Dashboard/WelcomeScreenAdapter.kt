package hypr.a255bits.com.hypr.Dashboard

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.pawegio.kandroid.alert
import hypr.a255bits.com.hypr.BuyGenerator
import hypr.a255bits.com.hypr.R
import kotlinx.android.synthetic.main.model_card.view.*
import org.greenrobot.eventbus.EventBus


class WelcomeScreenAdapter(val generators: MutableList<BuyGenerator>, val context: Context, val generatorImages: List<Bitmap>) : RecyclerView.Adapter<WelcomeScreenAdapter.CustomViewHolder>() {

    override fun getItemCount(): Int = generators.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.model_card, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        hideBuyButtonIfBought(position, holder)
        buyButtonClickListener(holder, position)

        populateListItem(holder, position)
    }

    private fun populateListItem(holder: CustomViewHolder?, position: Int) {
        holder?.title?.text = generators[position].name
        holder?.card?.setOnClickListener {EventBus.getDefault().post(position.toDouble()) }
        holder?.cardImage?.setImageBitmap(generatorImages[position])
    }

    private fun buyButtonClickListener(holder: CustomViewHolder?, position: Int) {
        holder?.buyButton?.setOnClickListener {
            context.alert(context.getString(R.string.buy_model_popup_message), "Hypr") {
                positiveButton("Buy", { EventBus.getDefault().post(position) })
                negativeButton { dismiss() }
            }.show()
        }
    }

    private fun hideBuyButtonIfBought(position: Int, holder: CustomViewHolder) {
        if(generators[position].itemBought){
            holder.buyButton.visibility = View.INVISIBLE
            holder.boughtTag.visibility = View.VISIBLE
        }
    }

    class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.nameOfCard
        val boughtTag: TextView = view.boughtTag
        val buyButton: Button = view.buyButton
        val card: CardView = view.card
        val background: RelativeLayout = view.backgroundLayout
        val cardImage: ImageView = view.cardImage

    }
}
