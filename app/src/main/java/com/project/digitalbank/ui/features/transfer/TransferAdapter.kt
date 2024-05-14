package com.project.digitalbank.ui.features.transfer

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.project.digitalbank.R
import com.project.digitalbank.data.model.User
import com.project.digitalbank.databinding.TransferUserItemBinding
import com.squareup.picasso.Picasso

class TransferAdapter(
    private val context: Context,
    private val userSelected: (User) -> Unit
) : ListAdapter<User, TransferAdapter.ViewHolder>(TransferDiffCallback()) {

    class ViewHolder(val binding: TransferUserItemBinding) : RecyclerView.ViewHolder(binding.root)

    class TransferDiffCallback : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            TransferUserItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user: User = getItem(position)

        loadImage(holder, user)
        holder.binding.txtUserName.text = user.name

        holder.itemView.setOnClickListener {
            userSelected(user)
        }

    }

    private fun loadImage(holder: ViewHolder, user: User) {
    if (user.imageProfile.isNotEmpty()) {
            Picasso
                .get()
                .load(user.imageProfile)
                .into(holder.binding.imgUser)
        } else {
            holder.binding.imgUser.setImageResource(R.drawable.ic_user_place_holder)
        }
    }

}