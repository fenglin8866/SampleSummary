package com.sample.feature.logger.logs.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sample.feature.logger.R
import com.sample.feature.logger.logs.repository.Log


/**
 * RecyclerView adapter for the logs list.
 */
class LogsViewAdapter(
    var logsDataSet: List<Log>
) : RecyclerView.Adapter<LogsViewAdapter.LogsViewHolder>() {

    class LogsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.text)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.text_row_item, parent, false)
        return LogsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return logsDataSet.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: LogsViewHolder, position: Int) {
        val log = logsDataSet[position]
        holder.textView.text = "${log.msg}\n\tStartTime: ${log.startTimeStr}" +
                "\n\tEnd  Time: ${log.endTimeStr}\n\t   Duration: ${log.duration}ms"
    }
}