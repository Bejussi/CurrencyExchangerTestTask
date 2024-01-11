package com.bejussi.currencyexchangertesttask.core

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.bejussi.currencyexchangertesttask.data.local.model.BalanceDto
import com.bejussi.currencyexchangertesttask.data.local.model.CurrencyDto
import com.bejussi.currencyexchangertesttask.data.local.model.RatesDto
import com.bejussi.currencyexchangertesttask.data.local.model.TransactionDto
import com.bejussi.currencyexchangertesttask.data.remote.model.CurrencyResponceData
import com.bejussi.currencyexchangertesttask.data.remote.model.RatesData
import com.bejussi.currencyexchangertesttask.domain.model.Balance
import com.bejussi.currencyexchangertesttask.domain.model.CurrencyResponce
import com.bejussi.currencyexchangertesttask.domain.model.Rates
import com.bejussi.currencyexchangertesttask.domain.model.Transaction
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.reflect.KProperty1

fun Context.makeToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }
    })
}

fun Context.showTransactionDialog(title: String, message: String, positiveButtonText: String) {
    MaterialAlertDialogBuilder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveButtonText) { dialog, which ->
            dialog.dismiss()
        }
        .show()
}

fun Rates.getDoubleValueByName(currency: String): Double? {
    val property = Rates::class.members.find { it.name == currency }
    return when (property) {
        is KProperty1<*, *> -> property.call(this) as? Double ?: 1.0
        else -> null
    }
}

fun List<Balance>.getCurrencyBalance(currencyCode: String): Double {
    return this.find { it.currencyCode == currencyCode }?.balance
        ?: 0.0
}

fun Double.roundValue(): Double {
    return BigDecimal(this).setScale(2, RoundingMode.HALF_EVEN).toDouble()
}

fun Spinner.afterItemSelected(onItemSelected:(String) -> Unit) {
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
            onItemSelected(selectedItem.toString())
        }
        override fun onNothingSelected(p0: AdapterView<*>?) {}
    }
}

fun Transaction.mapTransactionToTransactionDto(): TransactionDto {
    return TransactionDto(
        id = this.id,
        toCurrency = this.toCurrency,
        fromCurrency = this.fromCurrency,
        sellAmount = this.sellAmount,
        receiveAmount = this.receiveAmount,
        commissionFee = this.commissionFee

    )
}

fun Balance.mapBalanceToBalanceDto(): BalanceDto {
    return BalanceDto(
        currencyCode = this.currencyCode,
        balance = this.balance
    )
}

fun BalanceDto.mapBalanceDtoToBalance(): Balance {
    return Balance(
        currencyCode = this.currencyCode,
        balance = this.balance
    )
}

fun CurrencyResponceData.mapCurrencyResponceDataToCurrencyDto(): CurrencyDto {
    return CurrencyDto(
        base = this.base,
        date = this.date,
        ratesData = this.ratesData.mapRatesDataToRatesDto()
    )
}

fun CurrencyDto?.mapCurrencyDtoToCurrencyResponce(): CurrencyResponce? {
    return this?.let {
        CurrencyResponce(
            base = it.base,
            date = it.date,
            rates = it.ratesData.mapRatesDtoToRates()
        )
    }
}

fun RatesData.mapRatesDataToRatesDto(): RatesDto {
    return RatesDto(
        AED = this.AED,
        AFN = this.AFN,
        ALL = this.ALL,
        AMD = this.AMD,
        ANG = this.ANG,
        AOA = this.AOA,
        ARS = this.ARS,
        AUD = this.AUD,
        AWG = this.AWG,
        AZN = this.AZN,
        BAM = this.BAM,
        BBD = this.BBD,
        BDT = this.BDT,
        BGN = this.BGN,
        BHD = this.BHD,
        BIF = this.BIF,
        BMD = this.BMD,
        BND = this.BND,
        BOB = this.BOB,
        BRL = this.BRL,
        BSD = this.BSD,
        BTC = this.BTC,
        BTN = this.BTN,
        BWP = this.BWP,
        BYN = this.BYN,
        BYR = this.BYR,
        BZD = this.BZD,
        CAD = this.CAD,
        CDF = this.CDF,
        CHF = this.CHF,
        CLF = this.CLF,
        CLP = this.CLP,
        CNY = this.CNY,
        COP = this.COP,
        CRC = this.CRC,
        CUC = this.CUC,
        CUP = this.CUP,
        CVE = this.CVE,
        CZK = this.CZK,
        DJF = this.DJF,
        DKK = this.DKK,
        DOP = this.DOP,
        DZD = this.DZD,
        EGP = this.EGP,
        ERN = this.ERN,
        ETB = this.ETB,
        EUR = this.EUR,
        FJD = this.FJD,
        FKP = this.FKP,
        GBP = this.GBP,
        GEL = this.GEL,
        GGP = this.GGP,
        GHS = this.GHS,
        GIP = this.GIP,
        GMD = this.GMD,
        GNF = this.GNF,
        GTQ = this.GTQ,
        GYD = this.GYD,
        HKD = this.HKD,
        HNL = this.HNL,
        HRK = this.HRK,
        HTG = this.HTG,
        HUF = this.HUF,
        IDR = this.IDR,
        ILS = this.ILS,
        IMP = this.IMP,
        INR = this.INR,
        IQD = this.IQD,
        IRR = this.IRR,
        ISK = this.ISK,
        JEP = this.JEP,
        JMD = this.JMD,
        JOD = this.JOD,
        JPY = this.JPY,
        KES = this.KES,
        KGS = this.KGS,
        KHR = this.KHR,
        KMF = this.KMF,
        KPW = this.KPW,
        KRW = this.KRW,
        KWD = this.KWD,
        KYD = this.KYD,
        KZT = this.KZT,
        LAK = this.LAK,
        LBP = this.LBP,
        LKR = this.LKR,
        LRD = this.LRD,
        LSL = this.LSL,
        LTL = this.LTL,
        LVL = this.LVL,
        LYD = this.LYD,
        MAD = this.MAD,
        MDL = this.MDL,
        MGA = this.MGA,
        MKD = this.MKD,
        MMK = this.MMK,
        MNT = this.MNT,
        MOP = this.MOP,
        MRO = this.MRO,
        MUR = this.MUR,
        MVR = this.MVR,
        MWK = this.MWK,
        MXN = this.MXN,
        MYR = this.MYR,
        MZN = this.MZN,
        NAD = this.NAD,
        NGN = this.NGN,
        NIO = this.NIO,
        NOK = this.NOK,
        NPR = this.NPR,
        NZD = this.NZD,
        OMR = this.OMR,
        PAB = this.PAB,
        PEN = this.PEN,
        PGK = this.PGK,
        PHP = this.PHP,
        PKR = this.PKR,
        PLN = this.PLN,
        PYG = this.PYG,
        QAR = this.QAR,
        RON = this.RON,
        RSD = this.RSD,
        RUB = this.RUB,
        RWF = this.RWF,
        SAR = this.SAR,
        SBD = this.SBD,
        SCR = this.SCR,
        SDG = this.SDG,
        SEK = this.SEK,
        SGD = this.SGD,
        SHP = this.SHP,
        SLL = this.SLL,
        SOS = this.SOS,
        SRD = this.SRD,
        STD = this.STD,
        SVC = this.SVC,
        SYP = this.SYP,
        SZL = this.SZL,
        THB = this.THB,
        TJS = this.TJS,
        TMT = this.TMT,
        TND = this.TND,
        TOP = this.TOP,
        TRY = this.TRY,
        TTD = this.TTD,
        TWD = this.TWD,
        TZS = this.TZS,
        UAH = this.UAH,
        UGX = this.UGX,
        USD = this.USD,
        UYU = this.UYU,
        UZS = this.UZS,
        VEF = this.VEF,
        VND = this.VND,
        VUV = this.VUV,
        WST = this.WST,
        XAF = this.XAF,
        XAG = this.XAG,
        XAU = this.XAU,
        XCD = this.XCD,
        XDR = this.XDR,
        XOF = this.XOF,
        XPF = this.XPF,
        YER = this.YER,
        ZAR = this.ZAR,
        ZMK = this.ZMK,
        ZMW = this.ZMW,
        ZWL = this.ZWL
    )
}

fun RatesDto.mapRatesDtoToRates(): Rates {
    return Rates(
        AED = this.AED,
        AFN = this.AFN,
        ALL = this.ALL,
        AMD = this.AMD,
        ANG = this.ANG,
        AOA = this.AOA,
        ARS = this.ARS,
        AUD = this.AUD,
        AWG = this.AWG,
        AZN = this.AZN,
        BAM = this.BAM,
        BBD = this.BBD,
        BDT = this.BDT,
        BGN = this.BGN,
        BHD = this.BHD,
        BIF = this.BIF,
        BMD = this.BMD,
        BND = this.BND,
        BOB = this.BOB,
        BRL = this.BRL,
        BSD = this.BSD,
        BTC = this.BTC,
        BTN = this.BTN,
        BWP = this.BWP,
        BYN = this.BYN,
        BYR = this.BYR,
        BZD = this.BZD,
        CAD = this.CAD,
        CDF = this.CDF,
        CHF = this.CHF,
        CLF = this.CLF,
        CLP = this.CLP,
        CNY = this.CNY,
        COP = this.COP,
        CRC = this.CRC,
        CUC = this.CUC,
        CUP = this.CUP,
        CVE = this.CVE,
        CZK = this.CZK,
        DJF = this.DJF,
        DKK = this.DKK,
        DOP = this.DOP,
        DZD = this.DZD,
        EGP = this.EGP,
        ERN = this.ERN,
        ETB = this.ETB,
        EUR = this.EUR,
        FJD = this.FJD,
        FKP = this.FKP,
        GBP = this.GBP,
        GEL = this.GEL,
        GGP = this.GGP,
        GHS = this.GHS,
        GIP = this.GIP,
        GMD = this.GMD,
        GNF = this.GNF,
        GTQ = this.GTQ,
        GYD = this.GYD,
        HKD = this.HKD,
        HNL = this.HNL,
        HRK = this.HRK,
        HTG = this.HTG,
        HUF = this.HUF,
        IDR = this.IDR,
        ILS = this.ILS,
        IMP = this.IMP,
        INR = this.INR,
        IQD = this.IQD,
        IRR = this.IRR,
        ISK = this.ISK,
        JEP = this.JEP,
        JMD = this.JMD,
        JOD = this.JOD,
        JPY = this.JPY,
        KES = this.KES,
        KGS = this.KGS,
        KHR = this.KHR,
        KMF = this.KMF,
        KPW = this.KPW,
        KRW = this.KRW,
        KWD = this.KWD,
        KYD = this.KYD,
        KZT = this.KZT,
        LAK = this.LAK,
        LBP = this.LBP,
        LKR = this.LKR,
        LRD = this.LRD,
        LSL = this.LSL,
        LTL = this.LTL,
        LVL = this.LVL,
        LYD = this.LYD,
        MAD = this.MAD,
        MDL = this.MDL,
        MGA = this.MGA,
        MKD = this.MKD,
        MMK = this.MMK,
        MNT = this.MNT,
        MOP = this.MOP,
        MRO = this.MRO,
        MUR = this.MUR,
        MVR = this.MVR,
        MWK = this.MWK,
        MXN = this.MXN,
        MYR = this.MYR,
        MZN = this.MZN,
        NAD = this.NAD,
        NGN = this.NGN,
        NIO = this.NIO,
        NOK = this.NOK,
        NPR = this.NPR,
        NZD = this.NZD,
        OMR = this.OMR,
        PAB = this.PAB,
        PEN = this.PEN,
        PGK = this.PGK,
        PHP = this.PHP,
        PKR = this.PKR,
        PLN = this.PLN,
        PYG = this.PYG,
        QAR = this.QAR,
        RON = this.RON,
        RSD = this.RSD,
        RUB = this.RUB,
        RWF = this.RWF,
        SAR = this.SAR,
        SBD = this.SBD,
        SCR = this.SCR,
        SDG = this.SDG,
        SEK = this.SEK,
        SGD = this.SGD,
        SHP = this.SHP,
        SLL = this.SLL,
        SOS = this.SOS,
        SRD = this.SRD,
        STD = this.STD,
        SVC = this.SVC,
        SYP = this.SYP,
        SZL = this.SZL,
        THB = this.THB,
        TJS = this.TJS,
        TMT = this.TMT,
        TND = this.TND,
        TOP = this.TOP,
        TRY = this.TRY,
        TTD = this.TTD,
        TWD = this.TWD,
        TZS = this.TZS,
        UAH = this.UAH,
        UGX = this.UGX,
        USD = this.USD,
        UYU = this.UYU,
        UZS = this.UZS,
        VEF = this.VEF,
        VND = this.VND,
        VUV = this.VUV,
        WST = this.WST,
        XAF = this.XAF,
        XAG = this.XAG,
        XAU = this.XAU,
        XCD = this.XCD,
        XDR = this.XDR,
        XOF = this.XOF,
        XPF = this.XPF,
        YER = this.YER,
        ZAR = this.ZAR,
        ZMK = this.ZMK,
        ZMW = this.ZMW,
        ZWL = this.ZWL
    )
}