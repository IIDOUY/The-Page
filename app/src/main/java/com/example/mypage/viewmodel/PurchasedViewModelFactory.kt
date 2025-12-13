package com.example.mypage.viewmodel



import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mypage.repository.PurchasedRepository

class PurchasedViewModelFactory(
    private val repository: PurchasedRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PurchasedViewModel(repository) as T
    }
}
