package hypr.gan.com.hypr.Dashboard

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.pawegio.kandroid.alert
import com.pawegio.kandroid.inflateLayout
import hypr.gan.com.hypr.BuyGenerator
import hypr.gan.com.hypr.R
import kotlinx.android.synthetic.main.model_card.view.*
import org.greenrobot.eventbus.EventBus


class WelcomeScreenAdapter(val generators: MutableList<BuyGenerator>, val context: Context, val generatorImages: List<Bitmap>, val modelDescriptions: List<String>) : RecyclerView.Adapter<WelcomeScreenAdapter.CustomViewHolder>() {

    override fun getItemCount(): Int = generators.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomViewHolder {
        val view = context.inflateLayout(R.layout.model_card, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {
        hideBuyButtonIfBought(position, holder?.buyButton)
        buyButtonClickListener(holder, position)

        populateListItem(holder, position)
    }

    private fun populateListItem(holder: CustomViewHolder?, position: Int) = with(holder!!) {
        title.text = generators[position].name
        card.setOnClickListener { EventBus.getDefault().post(position.toDouble()) }
        cardImage.setImageBitmap(generatorImages[position])
    }

    private fun buyButtonClickListener(holder: CustomViewHolder?, position: Int) {
        val description = modelDescriptions[position]
        holder?.buyButton?.setOnClickListener {
            context.alert(context.getString(R.string.buy_model_popup_message) + " \n\n$description", generators[position].name) {
                positiveButton("Buy", { EventBus.getDefault().post(position) })
                negativeButton { dismiss() }
            }.show()
        }
    }

    private fun hideBuyButtonIfBought(position: Int, buyButton: Button?) {
        if (generators[position].itemBought) {
            buyButton?.visibility = View.INVISIBLE
        }
    }

    class CustomViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.nameOfCard
        val buyButton: Button = view.buyButton
        val card: CardView = view.card
        val background: RelativeLayout = view.backgroundLayout
        val cardImage: ImageView = view.cardImage

    }
}
