package com.sample.core.basic.ui.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sample.core.basic.databinding.ItemMainBinding

class TextAdapter(private var mContext: Context, private var mList: List<String>) :
    RecyclerView.Adapter<TextAdapter.VH>() {

    private var mListener: ItemClickListener? = null


    fun setItemClickListener(mListener: ItemClickListener) {
        this.mListener = mListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding: ItemMainBinding =
            ItemMainBinding.inflate(LayoutInflater.from(mContext), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val name = mList[position]
        holder.mBinding.name.text = name
        holder.mBinding.cardView.setOnClickListener { mListener!!.onItemClick(name, position) }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class VH(binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root) {
        var mBinding: ItemMainBinding = binding
    }

    fun interface ItemClickListener {
        fun onItemClick(name: String, position: Int)
    }
}