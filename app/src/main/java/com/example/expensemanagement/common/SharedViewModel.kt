//package com.example.expensemanagement.common
//
//import androidx.lifecycle.ViewModel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//
//class SharedViewModel : ViewModel() {
//    private val _showSnackbar = MutableStateFlow(false)
//    val showSnackbar: StateFlow<Boolean> = _showSnackbar
//
//    fun triggerSnackbar() {
//        _showSnackbar.value = true
//    }
//
//    fun resetSnackbar() {
//        _showSnackbar.value = false
//    }
//}