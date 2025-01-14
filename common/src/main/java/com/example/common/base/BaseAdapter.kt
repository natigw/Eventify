package com.example.common.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class BaseAdapter<VB : ViewBinding>(val inflater: (LayoutInflater, ViewGroup?, Boolean) -> VB) : RecyclerView.Adapter<BaseAdapter.BaseViewHolder<VB>>() {
    class BaseViewHolder<VB : ViewBinding>(val binding: VB) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VB> {
        return BaseViewHolder(inflater(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: BaseViewHolder<VB>, position: Int) {
        onBindLight(holder.binding, position)
    }

    abstract fun onBindLight(binding: VB, position: Int)

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}