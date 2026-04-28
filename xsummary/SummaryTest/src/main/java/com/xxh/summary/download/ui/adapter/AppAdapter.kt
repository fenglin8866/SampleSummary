package com.xxh.summary.download.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.xxh.summary.databinding.ItemAppBinding
import com.xxh.summary.download.model.AppUIModel
import com.xxh.summary.download.model.ClickType
import com.xxh.summary.download.model.Status

class AppAdapter(
    private val onClick: (ClickType,AppUIModel) -> Unit
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
        val item = getItem(position)
        if (payloads.isNotEmpty()) {
            Log.i("xxh0000", "onBindViewHolder position=$position")
            updateDownload(holder, item)
        } else {
            Log.i("xxh0001", "onBindViewHolder position=$position")
            bindFull(holder, item)
        }
        holder.binding.btnAction.setOnClickListener {
            onClick(ClickType.DOWNLOAD,item)
        }
        holder.binding.root.setOnClickListener {
            onClick(ClickType.ITEM,item)
        }
    }

    private fun bindFull(holder: VH, item: AppUIModel) {
        holder.binding.tvAppName.text = item.name
        holder.binding.tvPackageName.text = item.packageName
        holder.binding.tvDownloadCount.text = item.summary
        updateDownload(holder, item)
        Glide.with(holder.binding.ivAppIcon.context).load(item.icon).into(holder.binding.ivAppIcon)
    }

    private fun updateDownload(holder: VH, item: AppUIModel) {
        holder.binding.progressDownload.progress = item.progress
        Log.i("xxh0002", "status = ${item.status}")
        val str = when (item.status) {
            Status.DOWNLOADING -> "暂停"
            Status.PAUSED -> "继续"
            Status.SUCCESS -> "安装"
            Status.INSTALLING -> "安装中"
            Status.INSTALLED -> "打开"
            Status.FAILED -> "重试"
            else -> "下载"
        }
        Log.i("xxh122", "status = ${item.status} str=$str")
        holder.binding.btnAction.text = str
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