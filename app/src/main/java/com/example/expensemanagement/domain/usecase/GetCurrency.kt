package com.example.expensemanagement.domain.usecase

import com.example.expensemanagement.domain.models.CurrencyModel
import java.util.Currency
import java.util.Locale
import javax.inject.Inject


class GetCurrency @Inject constructor() {
    operator fun invoke(): List<CurrencyModel> {
        val currencies = mutableSetOf<CurrencyModel>()
        val countries = mutableSetOf<String>()
        val allLocale = Locale.getAvailableLocales()

        allLocale.forEach { locale ->
            val countryName = locale.displayCountry
            try {
                val currency = Currency.getInstance(locale)
                val currencyCode = currency.currencyCode
                val currencySymbol = currency.symbol

                val currencyModel = CurrencyModel(countryName, currencyCode, currencySymbol)
                countries.add(countryName)
                currencies.add(currencyModel)

            } catch (e: Exception) { }
        }
        return currencies.sortedBy { it.country }
    }
}