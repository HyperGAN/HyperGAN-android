package hypr.a255bits.com.hypr.WelcomeScreen

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import hypr.a255bits.com.hypr.BuyGenerator
import hypr.a255bits.com.hypr.R
import kotlinx.android.synthetic.main.model_card.view.*


class WelcomeScreenAdapter(val generators: Array<BuyGenerator>): RecyclerView.Adapter<WelcomeScreenAdapter.CustomViewHolder>() {

    override fun getItemCount(): Int {
        return generators.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomViewHolder {
        val view = LayoutInflater.from(parent?.context).inflate(R.layout.model_card, parent, false)
        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder?, position: Int) {
        holder?.title?.text = generators[position].name
    }

    class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val title: TextView = view.nameOfCard
    }

}