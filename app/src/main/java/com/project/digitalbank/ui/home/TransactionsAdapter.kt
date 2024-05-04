package com.project.digitalbank.ui.home

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.digitalbank.R
import com.project.digitalbank.data.enum.TransactionOperation
import com.project.digitalbank.data.enum.TransactionType
import com.project.digitalbank.data.model.Transaction
import com.project.digitalbank.databinding.ItemTransactionBinding
import com.project.digitalbank.util.GetMask

class TransactionsAdapter(
    private val context: Context,
    private val transactionSelected: (Transaction) -> Unit
) : ListAdapter<Transaction, TransactionsAdapter.ViewHolder>(TransactionDiffCallback()) {

    class ViewHolder(val binding: ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    class TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.id == newItem.id
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemTransactionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = getItem(position)

        holder.binding.apply {
            txtTransactionDate.text = GetMask.getFormattedDate(transaction.date, GetMask.DAY_MONTH_YEAR_HOUR_MINUTE)
            txtTransactionDescription.text = transaction.operation?.toString() ?: "NULL"
            txtTransactionValue.text = GetMask.getFormattedValue(transaction.value)
            txtTransactionType.text = transaction.operation?.let { TransactionOperation.getType(it).toString() }
            txtTransactionType.backgroundTintList =
                if (transaction.type == TransactionType.CASH_IN) {
                    ColorStateList.valueOf(context.getColor(R.color.darker_green))
                } else {
                    ColorStateList.valueOf(context.getColor(R.color.red_cash_out))
                }


        }
        holder.itemView.setOnClickListener {
            transactionSelected(transaction)
        }

    }

}