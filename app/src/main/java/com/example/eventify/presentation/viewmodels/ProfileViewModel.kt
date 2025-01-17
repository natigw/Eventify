package com.example.eventify.presentation.viewmodels

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.auth.UserData
import com.example.domain.model.places.FavoriteItem
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class ProfileViewModel @Inject constructor(
    @Named("LanguageChoice")
    var sharedPrefLanguage: SharedPreferences,
    @Named("ThemeChoice")
    var sharedPrefTheme: SharedPreferences,
    private val authRepository: AuthRepository,
    private val eventRepository: EventRepository
): ViewModel() {

    val userData = MutableStateFlow<UserData?>(null)
    val favorites = MutableStateFlow<List<FavoriteItem>?>(null)

    init {
        getUserData()
        getFavorites()
    }

    private fun getUserData() {
        viewModelScope.launch {
            val data = authRepository.getUserData()
            userData.update { data }
        }
    }

    fun getFavorites() {
        viewModelScope.launch {
            val favoriteResponse = eventRepository.getFavEvents()
            favorites.update { favoriteResponse }
        }
    }
}