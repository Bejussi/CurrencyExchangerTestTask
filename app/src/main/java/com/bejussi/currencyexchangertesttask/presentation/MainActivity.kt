package com.bejussi.currencyexchangertesttask.presentation

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.bejussi.currencyexchangertesttask.R
import com.bejussi.currencyexchangertesttask.core.UIEvent
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

        setupCurrencyAdapters()
        setupBalancesRecyclerView()

        lifecycleScope.launch {
            mainViewModel.balances.collect { balancesList ->
                balancesAdapter.submitList(balancesList)
            }
        }

        lifecycleScope.launch {
            mainViewModel.state.collect { state ->
                binding.receiveMoneyValue.text =
                    getString(R.string.receive_amount, state.receiveAmount.toString())
                binding.internetText.visibility =
                    if (state.isInternetAvailable) View.VISIBLE else View.GONE
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

        binding.sellMoneyEditText.doOnTextChanged { inputText, _, _, _ ->
            val text = inputText.toString().trim()
            if (text.isNotEmpty()) {
                sendEvent(MainEvent.SetSellAmount(sellAmount = text.toDouble()))
            } else {
                sendEvent(MainEvent.SetSellAmount(sellAmount = 0.0))
            }
        }

        binding.sellRatesAutoCompleteTextView.setOnItemClickListener { adapterView, view, position, l ->
            val selectedItem = adapterView.getItemAtPosition(position).toString()
            sendEvent(MainEvent.SetSellCurrency(sellCurrency = selectedItem))
        }

        binding.receiveRatesAutoCompleteTextView.setOnItemClickListener { adapterView, view, position, l ->
            val selectedItem = adapterView.getItemAtPosition(position).toString()
            sendEvent(MainEvent.SetReceiveCurrency(receiveCurrency = selectedItem))
        }

        binding.submitButton.setOnClickListener {
            sendEvent(MainEvent.SubmitTransaction)
        }
    }

    override fun onResume() {
        super.onResume()
        setupCurrencyAdapters()
    }

    private fun setupCurrencyAdapters() {
        val sellCurrencyAdapter = createCurrencyAdapter(R.array.sell_currency_codes)
        val receiveCurrencyAdapter = createCurrencyAdapter(R.array.receive_currency_codes)

        binding.sellRatesAutoCompleteTextView.setAdapter(sellCurrencyAdapter)
        binding.receiveRatesAutoCompleteTextView.setAdapter(receiveCurrencyAdapter)
    }

    private fun createCurrencyAdapter(arrayResId: Int): ArrayAdapter<String> {
        return ArrayAdapter(this, R.layout.dropdown_item, resources.getStringArray(arrayResId))
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