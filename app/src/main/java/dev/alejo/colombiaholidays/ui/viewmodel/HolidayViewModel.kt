package dev.alejo.colombiaholidays.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.alejo.colombiaholidays.data.model.HolidayModel
import dev.alejo.colombiaholidays.domain.GetHolidayUseCase
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HolidayViewModel @Inject constructor(
    private val getHolidayUseCase: GetHolidayUseCase
): ViewModel(){

    val holidayByYearResponse = MutableLiveData<List<HolidayModel>>()
    val currentHolidayResponse = MutableLiveData<HolidayModel>()

    fun onCreate() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()
        getHolidayByYear(currentYear)
        // Get today is holiday
    }

    fun getHolidayByYear(year: String) {
        viewModelScope.launch {
            val result = getHolidayUseCase(year)
            if(result.isNotEmpty())
                holidayByYearResponse.postValue(result)
        }
    }

}