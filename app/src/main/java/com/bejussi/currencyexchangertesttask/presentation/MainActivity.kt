package com.bejussi.currencyexchangertesttask.presentation

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bejussi.currencyexchangertesttask.R
import com.bejussi.currencyexchangertesttask.core.UIEvent
import com.bejussi.currencyexchangertesttask.core.afterItemSelected
import com.bejussi.currencyexchangertesttask.core.afterTextChanged
import com.bejussi.currencyexchangertesttask.core.makeToast
import com.bejussi.currencyexchangertesttask.core.showTransactionDialog
import com.bejussi.currencyexchangertesttask.databinding.ActivityMainBinding
import com.bejussi.currencyexchangertesttask.presentation.main.MainViewModel
import com.bejussi.currencyexchangertesttask.presentation.main.adapter.BalancesAdapter
import com.bejussi.currencyexchangertesttask.presentation.main.adapter.BalancesItemDecoration
import com.bejussi.currencyexchangertesttask.presentation.main.model.MainEvent
import kotlinx.coroutines.flow.collectLatest
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
            mainViewModel.state.collect { state ->
                binding.receiveMoneyValue.text = state.receiveAmount.toString()
                binding.internetText.visibility =
                    if (state.isInternetAvailable) View.GONE else View.VISIBLE
                binding.submitButton.isEnabled = state.isSubmitAvailable
            }
        }

        lifecycleScope.launch {
            mainViewModel.eventFlow.collectLatest { event ->
                when (event) {
                    is UIEvent.ShowToast -> makeToast(event.message)
                    is UIEvent.ShowSuccessDialog -> showTransactionDialog(
                        title = getString(R.string.dialog_title),
                        message = getString(
                            R.string.dialog_message,
                            event.transaction.sellAmount.toString(),
                            event.transaction.fromCurrency,
                            event.transaction.receiveAmount.toString(),
                            event.transaction.toCurrency,
                            event.transaction.commissionFee.toString(),
                            event.transaction.fromCurrency
                        ),
                        positiveButtonText = getString(R.string.dialog_button)
                    )
                }
            }
        }

        binding.sellMoneyValue.afterTextChanged { value ->
            if (value.isNotEmpty()) {
                sendEvent(MainEvent.SetSellAmount(sellAmount = value.toDouble()))
            } else {
                sendEvent(MainEvent.SetSellAmount(sellAmount = 0.0))
            }
        }

        binding.sellRatesSpinner.afterItemSelected { selectedItem ->
            sendEvent(MainEvent.SetSellCurrency(sellCurrency = selectedItem))
        }

        binding.receiveRatesSpinner.afterItemSelected { selectedItem ->
            sendEvent(MainEvent.SetReceiveCurrency(receiveCurrency = selectedItem))
        }

        binding.submitButton.setOnClickListener {
            sendEvent(MainEvent.SubmitTransaction)
        }
    }

    private fun sendEvent(mainEvent: MainEvent) = mainViewModel.onEvent(mainEvent)

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