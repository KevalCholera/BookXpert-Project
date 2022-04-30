package com.example.bookxpertproject.design.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.bookxpertproject.databinding.ItemBookxpertBinding
import com.example.bookxpertproject.model.DashboardBookXpressResponse

class DashboardBookXpertAdapter(
    private val albumResponse: List<DashboardBookXpressResponse.Data>
) : RecyclerView.Adapter<DashboardBookXpertAdapter.BooxXpertDataViewHolder>() {
    private lateinit var binding: ItemBookxpertBinding

    class BooxXpertDataViewHolder(
        private val binding: ItemBookxpertBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(responsePositionalData: DashboardBookXpressResponse.Data) {

            binding.tvItemIndex.text = responsePositionalData.actID.toString()
            binding.tvItemName.text = responsePositionalData.actName.toString()
            binding.tvItemAmount.text = responsePositionalData.amount.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BooxXpertDataViewHolder {
        binding = ItemBookxpertBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return BooxXpertDataViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return albumResponse.size
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun onBindViewHolder(holder: BooxXpertDataViewHolder, position: Int) {
        val responsePositionalData = albumResponse[position]
        holder.bind(responsePositionalData)
    }

}