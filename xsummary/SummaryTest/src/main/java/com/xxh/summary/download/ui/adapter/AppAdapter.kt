package com.xxh.summary.download.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xxh.summary.databinding.ItemAppBinding
import com.xxh.summary.download.model.AppUIModel
import com.xxh.summary.download.model.Status

class AppAdapter(
    private val onClick: (AppUIModel) -> Unit
) : ListAdapter<AppUIModel, AppAdapter.VH>(Diff()) {

    class VH(val binding: ItemAppBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemAppBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        bindFull(holder, getItem(position))
    }

    override fun onBindViewHolder(
        holder: VH,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            updateDownload(holder, getItem(position))
        } else {
            bindFull(holder, getItem(position))
        }
    }

    private fun bindFull(holder: VH, item: AppUIModel) {
        holder.binding.tvAppName.text = item.name
        holder.binding.tvPackageName.text = item.packageName
        updateDownload(holder, item)

        holder.binding.btnAction.setOnClickListener {
            onClick(item)
        }
    }

    private fun updateDownload(holder: VH, item: AppUIModel) {
        holder.binding.progressDownload.progress = item.progress
        Log.i("xxh122","status = ${item.status}")
        val str= when (item.status) {
            Status.DOWNLOADING -> "暂停"
            Status.PAUSED -> "继续"
            Status.SUCCESS -> "安装"
            Status.INSTALLING -> "安装中"
            Status.INSTALLED -> "打开"
            Status.FAILED -> "下载失败"
            else -> "下载"
        }
        Log.i("xxh122","status = ${item.status} str=$str")
        holder.binding.btnAction.text=str
    }

    class Diff : DiffUtil.ItemCallback<AppUIModel>() {

        override fun areItemsTheSame(o: AppUIModel, n: AppUIModel) =
            o.packageName == n.packageName

        override fun areContentsTheSame(o: AppUIModel, n: AppUIModel) =
            o == n

        override fun getChangePayload(o: AppUIModel, n: AppUIModel): Any? {
            return if (o.progress != n.progress || o.status != n.status) {
                true
            } else null
        }
    }
}