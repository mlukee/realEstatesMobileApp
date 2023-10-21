package com.example.poraapplication

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.lib.RealEstate
import com.example.poraapplication.databinding.ListItemBinding

class RealEstateAdapter(private val realEstates: MutableList<RealEstate>) : RecyclerView.Adapter<RealEstateAdapter.RealEstateViewHolder>() {

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
        return realEstates.size
    }

    fun addRealEstate(realEstate: RealEstate) {
        realEstates.add(realEstate)
        notifyItemInserted(realEstates.size - 1)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RealEstateViewHolder, position: Int) {
        val realEstate = realEstates[position]
        holder.propertyType.text = realEstate.propertyType
        holder.area.text = "${realEstate.area} m²"
        holder.price.text = "${realEstate.price} €"
        holder.image.setImageResource(R.drawable.home96)
    }
}