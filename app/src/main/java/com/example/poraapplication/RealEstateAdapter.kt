package com.example.poraapplication

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lib.RealEstate
import com.example.poraapplication.databinding.ListItemBinding
import java.text.NumberFormat
import java.util.Locale


class RealEstateAdapter(private val app: MyApplication) : RecyclerView.Adapter<RealEstateAdapter.RealEstateViewHolder>() {

    class RealEstateViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        private val binding = ListItemBinding.bind(itemView)
        val image = binding.imageView
        val propertyType = binding.tvListItemHeading
        val area = binding.tvListItemBody
        val price = binding.tvListItemPrice
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RealEstateViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return RealEstateViewHolder(view)
    }

    override fun getItemCount(): Int {
        return app.transactions.getRealEstateSize()
    }

    fun addRealEstate(realEstate: RealEstate) {
        app.transactions.addRealEstate(realEstate)
        notifyItemInserted(app.transactions.realEstates.size - 1)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RealEstateViewHolder, position: Int) {
        val realEstate = app.transactions.realEstates[position]
        holder.propertyType.text = realEstate.propertyType
        holder.area.text = String.format("%.2f m²", realEstate.area)
        val numberFormat: NumberFormat = NumberFormat.getNumberInstance(Locale.GERMANY)
        numberFormat.minimumFractionDigits = 2
        numberFormat.maximumFractionDigits = 2
        val formattedPrice: String = numberFormat.format(realEstate.price)

        holder.price.text = "$formattedPrice €"
        holder.image.setImageResource(R.drawable.home96)
    }
}