package com.bejussi.currencyexchangertesttask.presentation.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bejussi.currencyexchangertesttask.databinding.BalanceItemBinding
import com.bejussi.currencyexchangertesttask.domain.model.Balance

class BalancesAdapter : ListAdapter<Balance, BalancesAdapter.BalanceViewHolder>(Diff()) {

    inner class BalanceViewHolder(
        private val binding: BalanceItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(balance: Balance) {
            binding.apply {
                currencyCode.text = balance.currencyCode
                currencyBalance.text = balance.balance.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BalanceViewHolder {
        return BalanceViewHolder(
            BalanceItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BalanceViewHolder, position: Int) {
        val currentBalance = getItem(position)
        holder.bind(currentBalance)
    }

    class Diff : DiffUtil.ItemCallback<Balance>() {
        override fun areItemsTheSame(oldItem: Balance, newItem: Balance): Boolean {
            return oldItem.currencyCode == newItem.currencyCode
        }

        override fun areContentsTheSame(oldItem: Balance, newItem: Balance): Boolean {
            return oldItem == newItem
        }
    }
}