package com.example.romero.proyectofinalappsmoviles.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.romero.proyectofinalappsmoviles.R
import com.example.romero.proyectofinalappsmoviles.data.local.entity.ReportEntity
import com.example.romero.proyectofinalappsmoviles.databinding.ItemReportBinding
import com.example.romero.proyectofinalappsmoviles.util.DateUtils

class ReportAdapter(
    private val onItemClick: (ReportEntity) -> Unit
) : ListAdapter<ReportEntity, ReportAdapter.ViewHolder>(Diff) {

    inner class ViewHolder(private val b: ItemReportBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(r: ReportEntity) {
            b.tvTitle.text      = r.title
            b.tvMeta.text       = "${r.category} · ${r.priority} · ${DateUtils.timeAgo(r.date)}"
            b.tvIconLetter.text = r.category.firstOrNull()?.uppercaseChar()?.toString() ?: "?"

            val bg = when (r.status) {
                "ABIERTO"    -> R.drawable.bg_status_open
                "EN_PROCESO" -> R.drawable.bg_status_progress
                else         -> R.drawable.bg_status_closed
            }
            val label = when (r.status) {
                "ABIERTO"    -> "Abierto"
                "EN_PROCESO" -> "En proceso"
                else         -> "Cerrado"
            }
            b.tvStatus.setBackgroundResource(bg)
            b.tvStatus.text = label

            b.ivSyncIcon.visibility =
                if (r.isSynced) android.view.View.GONE else android.view.View.VISIBLE

            b.root.setOnClickListener { onItemClick(r) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemReportBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    companion object Diff : DiffUtil.ItemCallback<ReportEntity>() {
        override fun areItemsTheSame(a: ReportEntity, b: ReportEntity) = a.id == b.id
        override fun areContentsTheSame(a: ReportEntity, b: ReportEntity) = a == b
    }
}