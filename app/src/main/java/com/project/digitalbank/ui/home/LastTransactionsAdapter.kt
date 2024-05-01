package com.project.digitalbank.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.digitalbank.data.model.Transaction
import com.project.digitalbank.databinding.ItemLastTransactionBinding
import com.project.digitalbank.util.GetMask

class LastTransactionsAdapter(
    private val transactionSelected: (Transaction) -> Unit
) : ListAdapter<Transaction, LastTransactionsAdapter.ViewHolder>(TransactionDiffCallback()){

    class ViewHolder(val binding: ItemLastTransactionBinding): RecyclerView.ViewHolder(binding.root) {

    }

    class TransactionDiffCallback: DiffUtil.ItemCallback<Transaction>() {
        override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
            return oldItem.id == newItem.id &&  oldItem.description == newItem.description
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemLastTransactionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaction = getItem(position)
        holder.binding.apply {
            txtTransactionDate.text = GetMask.getFormattedDate(transaction.date, GetMask.DAY_MONTH_YEAR_HOUR_MINUTE)
            txtTransactionDescription.text = transaction.description
            txtTransactionValue.text = GetMask.getFormattedValue(transaction.value)
            txtTransactionType.text = when(transaction.description) {
                "Transaction" -> "T"
                "Mobile Recharge" -> "R"
                "Deposit" -> "D"
                else -> ""
            }
        }

    }

}