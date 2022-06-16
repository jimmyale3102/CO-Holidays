package dev.alejo.colombian_holidays.ui.viewmodel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.alejo.colombian_holidays.R
import dev.alejo.colombian_holidays.core.Constants.Companion.CODE_200
import dev.alejo.colombian_holidays.core.Constants.Companion.CODE_204
import dev.alejo.colombian_holidays.core.DateUtils
import dev.alejo.colombian_holidays.data.model.HolidayModel
import dev.alejo.colombian_holidays.domain.GetHolidayUseCase
import dev.alejo.colombian_holidays.domain.GetNextPublicHolidayUserCase
import dev.alejo.colombian_holidays.domain.GetTodayHolidayUseCase
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class HolidayViewModel @Inject constructor(
    private val getHolidayUseCase: GetHolidayUseCase,
    private val getTodayHolidayUseCase: GetTodayHolidayUseCase,
    private val getNextPublicHolidayUseCase: GetNextPublicHolidayUserCase
): ViewModel(){

    val holidayByYearResponse = MutableLiveData<List<HolidayModel>>()
    val nextPublicHolidayResponse = MutableLiveData<HolidayModel>()
    val todayHolidayResponse = MutableLiveData<String>()
    private val todayHolidayDisplayed = MutableLiveData<Boolean>()

    fun onCreate(context: Context) {
        todayHolidayDisplayed.postValue(false)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR).toString()
        getHolidayByYear(currentYear)
        getNextHolidayByYear()
        getTodayHoliday(context)
    }

    private fun getTodayHoliday(context: Context) {
        viewModelScope.launch {
            getTodayHolidayUseCase().let { responseCode ->
                if(responseCode != CODE_200) {
                    todayHolidayDisplayed.postValue(true)
                    todayHolidayResponse.postValue(
                        when(responseCode) {
                            CODE_200 -> context.getString(R.string.today_is_holiday)
                            CODE_204 -> context.getString(R.string.today_is_not_holiday)
                            else -> context.getString(R.string.today_is_not_holiday)
                        }
                    )
                }
            }
        }
    }

    fun getNextHolidayByYear() {
        viewModelScope.launch {
            val result = getNextPublicHolidayUseCase()
            if(result.isNotEmpty())
                nextPublicHolidayResponse.postValue(result[0])
        }
    }

    fun getHolidayByYear(year: String) {
        viewModelScope.launch {
            val result = getHolidayUseCase(year)
            if(result.isNotEmpty()) {
                holidayByYearResponse.postValue(result)
                val todayHolidayName = getTodayHolidayName(result)
                if(todayHolidayDisplayed.value == false && todayHolidayName.isNotEmpty()) {
                    todayHolidayDisplayed.postValue(true)
                    todayHolidayResponse.postValue(todayHolidayName)
                }
            }
        }
    }

    private fun getTodayHolidayName(allHolidays: List<HolidayModel>): String {
        val currentDate = Calendar.getInstance()
        val holiday = allHolidays.filter { it.date == DateUtils.getStringDateFromDate(currentDate.time) }
        if(holiday.isNotEmpty())
            return if(Locale.getDefault().language == "en") holiday[0].name else holiday[0].localName
        return ""
    }

}