package com.ubaya.protectcare23.ui.history

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ubaya.protectcare23.data.Checks
import com.ubaya.protectcare23.databinding.ItemHistoryBinding

class HistoryAdapter(
    private val checks: List<Checks>,
    val context: Context
):RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val check = checks[position]
        holder.bind(check)
    }

    override fun getItemCount() = checks.size

    class HistoryViewHolder(val binding: ItemHistoryBinding, val context: Context): RecyclerView.ViewHolder(binding.root) {
        fun bind(check: Checks) {
            with(binding) {
                txtLocation.setText(check.loc_name)
                txtCheckin.setText(check.checkIn)
                txtCheckout.text = when {
                    check.checkOut == "null" -> "Not Yet"
                    else -> check.checkOut
                }

                itemView.backgroundTintList = ColorStateList.valueOf(Color.parseColor(if (check.checkOut == "null") "#FFFF00" else "#00FF00"))
            }
        }

    }
}