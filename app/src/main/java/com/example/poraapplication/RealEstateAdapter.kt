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


class RealEstateAdapter(
    private val app: MyApplication,
) : RecyclerView.Adapter<RealEstateAdapter.RealEstateViewHolder>() {

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(realEstate: RealEstate)
        fun onItemLongClick(realEstate: RealEstate): Boolean
    }

    class RealEstateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ListItemBinding.bind(itemView)
        val image = binding.imageView
        val propertyType = binding.tvListItemHeading
        val area = binding.tvListItemBody
        val price = binding.tvListItemPrice

        fun bind(realEstate: RealEstate, listener: OnItemClickListener) {
            itemView.setOnClickListener {
                listener.onItemClick(realEstate)
            }
            itemView.setOnLongClickListener {
                listener.onItemLongClick(realEstate)
            }
        }
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
        app.file.writeText(app.transactions.serializeRealEstateList())
        notifyItemInserted(app.transactions.realEstates.size - 1)
    }

    fun removeRealEstate(realEstate: RealEstate) {
        val position = app.transactions.realEstates.indexOf(realEstate)
        if (position != -1) {
            app.transactions.removeRealEstate(realEstate)
            app.file.writeText(app.transactions.serializeRealEstateList())
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, app.transactions.realEstates.size)
        }
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

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(realEstate)
        }
        holder.itemView.setOnLongClickListener {
            onItemClickListener?.onItemLongClick(realEstate)
            true
        }

    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    fun updateRealEstate(realEstate: RealEstate, position: Int) {
        app.transactions.updateRealEstate(realEstate, position)
        app.file.writeText(app.transactions.serializeRealEstateList())
        notifyItemChanged(position)
    }
}