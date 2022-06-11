package dev.alejo.colombiaholidays.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.alejo.colombiaholidays.data.model.HolidayModel
import dev.alejo.colombiaholidays.domain.GetHolidayUseCase
import dev.alejo.colombiaholidays.domain.GetTodayHolidayUseCase
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HolidayViewModel @Inject constructor(
    private val getHolidayUseCase: GetHolidayUseCase,
    private val getTodayHolidayUseCase: GetTodayHolidayUseCase
): ViewModel(){

    val holidayByYearResponse = MutableLiveData<List<HolidayModel>>()
    val todayHolidayResponse = MutableLiveData<String>()

    fun onCreate() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()
        getHolidayByYear(currentYear)
        viewModelScope.launch {
            getTodayHolidayUseCase().let {
                todayHolidayResponse.postValue(it)
            }
        }
    }

    fun getHolidayByYear(year: String) {
        viewModelScope.launch {
            val result = getHolidayUseCase(year)
            if(result.isNotEmpty())
                holidayByYearResponse.postValue(result)
        }
    }

}