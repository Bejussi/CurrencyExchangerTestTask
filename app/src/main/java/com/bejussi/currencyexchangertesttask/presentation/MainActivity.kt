package com.bejussi.currencyexchangertesttask.presentation

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bejussi.currencyexchangertesttask.R
import com.bejussi.currencyexchangertesttask.core.UIEvent
import com.bejussi.currencyexchangertesttask.core.afterItemSelected
import com.bejussi.currencyexchangertesttask.core.afterTextChanged
import com.bejussi.currencyexchangertesttask.core.makeToast
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

        lifecycleScope.launch {
            mainViewModel.eventFlow.collect { event ->
                when (event) {
                    is UIEvent.ShowToast -> makeToast(event.message)
                }
            }
        }

        lifecycleScope.launch {
            mainViewModel.receiveAmount.collect { receiveAmount ->
                binding.receiveMoneyValue.text = receiveAmount.toString()
            }
        }

        binding.sellMoneyValue.afterTextChanged { value ->
            if (value.isEmpty() || value.toDouble().equals(0.0)) {
                makeToast(resources.getString(R.string.invalid_amount))
            } else {
                performConversion()
            }
        }

        binding.sellRatesSpinner.afterItemSelected {
            performConversion()
        }

        binding.receiveRatesSpinner.afterItemSelected {
            performConversion()
        }

    }

    private fun performConversion() {
        lifecycleScope.launch {
            mainViewModel.calculateConvertation(
                sellAmount = binding.sellMoneyValue.text.toString(),
                fromCurrency = binding.sellRatesSpinner.selectedItem.toString(),
                toCurrency = binding.receiveRatesSpinner.selectedItem.toString()
            )
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