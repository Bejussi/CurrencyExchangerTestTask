package com.bejussi.currencyexchangertesttask.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bejussi.currencyexchangertesttask.R
import com.bejussi.currencyexchangertesttask.databinding.ActivityMainBinding
import com.bejussi.currencyexchangertesttask.presentation.main.MainViewModel
import com.bejussi.currencyexchangertesttask.presentation.main.adapter.BalancesAdapter
import com.bejussi.currencyexchangertesttask.presentation.main.adapter.BalancesItemDecoration
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by inject()
    private lateinit var balancesAdapter: BalancesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBalancesRecyclerView()

        lifecycleScope.launch {
            mainViewModel.balances.collect { balancesList ->
                balancesAdapter.submitList(balancesList)
            }
        }
    }

    private fun setupBalancesRecyclerView() {
        balancesAdapter = BalancesAdapter()
        binding.balancesRecyclerView.apply {
            adapter = balancesAdapter
            addItemDecoration(
                BalancesItemDecoration(
                    spaceSize = resources.getDimensionPixelSize(R.dimen.balance_item_space)
                )
            )
        }
    }
}