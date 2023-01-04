package com.averin.android.developer.base.util

import android.content.Context
import com.averin.android.developer.core.R
import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.DateTimeZone.UTC
import org.joda.time.Days
import org.joda.time.LocalDate
import org.joda.time.LocalTime
import org.joda.time.Years
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import timber.log.Timber

val dateNow: DateTime
    get() = DateTime.now()

val localTimeZone: DateTimeZone
    get() = dateNow.zone

fun isToday(time: DateTime?): Boolean = LocalDate.now()
    .compareTo(LocalDate(time)) == 0

fun isYesterday(time: DateTime?): Boolean = LocalDate.now()
    .minusDays(1)
    .compareTo(LocalDate(time)) == 0

fun getYearsBetween(start: DateTime, end: DateTime) = Years.yearsBetween(
    start.toLocalDate(),
    end.toLocalDate()
).years

fun String?.getDaysBetween(): Int {
    if (this.isNullOrEmpty()) return 0
    return Days.daysBetween(
        dateNow.toLocalDate(),
        SERVER_DATE_FORMAT_WITH_TIME
            .parseDateTime(this)
            .toLocalDate()
    ).days
}

fun DateTime.isAfterDate(dateTime: DateTime): Boolean {
    return this.isAfter(dateTime)
}

fun DateTime.isBeforeDate(dateTime: DateTime): Boolean {
    return this.isBefore(dateTime)
}

fun DateTime.toServerFormat(): String? {
    return try {
        toString(SERVER_DATE_FORMAT)
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
}

fun DateTime.convertFormats(
    toFormat: DateTimeFormatter = UI_DATE_FORMAT
): String? {
    return try {
        toString(toFormat)
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
}

fun String?.fromUiToDateTimeFormat(): DateTime? {
    return try {
        if (this.isNullOrEmpty()) {
            return null
        }
        return UI_DATE_FORMAT
            .parseDateTime(this)
            .withZoneRetainFields(UTC)
            .withZone(localTimeZone)
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
}

fun String?.fromServerToDateTimeFormat(): DateTime? {
    return try {
        if (this.isNullOrEmpty()) {
            return null
        }
        return SERVER_DATE_FORMAT
            .parseDateTime(this)
            .withZoneRetainFields(UTC)
            .withZone(localTimeZone)
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
}

fun String?.convertFormats(
    fromFormat: DateTimeFormatter,
    toFormat: DateTimeFormatter
): String? {
    return try {
        if (this.isNullOrEmpty()) {
            return null
        }
        return fromFormat
            .parseDateTime(this)
            .withZoneRetainFields(UTC)
            .withZone(localTimeZone)
            .toString(toFormat)
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
}

fun Float?.toChatHeaderFormat(context: Context, showTodayTime: Boolean = true): String? {
    this ?: return null
    return try {
        this.toLong().toComfortableUiFormat(context, showTodayTime)
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
}

fun Long?.toComfortableUiFormat(context: Context, showTodayTime: Boolean = true): String? {
    this ?: return null
    return try {
        val dateTime = DateTime(this)
        getComfortableUiFormat(dateTime, context, showTodayTime)
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
}

// From 2022-12-16T12:40:59 format
fun String?.toComfortableUiFormat(context: Context, showTodayTime: Boolean = true): String? {
    this ?: return null
    return try {
        val dateTime = DateTime(this)
        getComfortableUiFormat(dateTime, context, showTodayTime)
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
}

// From 2022-12-27 format to timestamp
fun String?.toTimestamp(): Long {
    this ?: return 0L
    return try {
        SERVER_DATE_FORMAT
            .parseDateTime(this)
            .millis
    } catch (e: Exception) {
        Timber.e(e)
        0L
    }
}

private fun getComfortableUiFormat(
    dateTime: DateTime,
    context: Context,
    showTodayTime: Boolean = true
): String? {
    return when {
        isToday(dateTime) -> {
            if (showTodayTime) {
                dateTime.toString(TIME_FORMAT)
            } else {
                context.getString(R.string.today)
            }
        }
        isYesterday(dateTime) -> {
            context.getString(R.string.yesterday)
        }
        else -> {
            dateTime.toString(UI_DATE_FORMAT)
        }
    }
}

fun Long?.toTimeLeftFormat(context: Context, withTimeLeft: Boolean = true): String? {
    this ?: return null
    if (this == 0L) {
        return null
    }
    return try {
        val dateTime = DateTime(this * 1000)
        when {
            this < 60 -> {
                if (withTimeLeft) {
                    context.getString(R.string.time_sec_left, dateTime.toString(SS_TIME_LEFT_FORMAT))
                } else {
                    dateTime.toString(MM_SS_TIME_LEFT_FORMAT)
                }
            }
            this < 60 * 60 -> {
                if (withTimeLeft) {
                    context.getString(R.string.time_left, dateTime.toString(MM_SS_TIME_LEFT_FORMAT))
                } else {
                    dateTime.toString(MM_SS_TIME_LEFT_FORMAT)
                }
            }
            else -> {
                if (withTimeLeft) {
                    context.getString(R.string.time_left, dateTime.toString(HH_MM_SS_TIME_LEFT_FORMAT))
                } else {
                    dateTime.toString(HH_MM_SS_TIME_LEFT_FORMAT)
                }
            }
        }
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
}

fun Long?.toDateYearTimeFormat(withSeconds: Boolean = false): String? {
    this ?: return null
    if (this == 0L) {
        return null
    }
    return try {
        val dateTime = DateTime(this)
        val formatter = if (withSeconds) {
            UI_DATE_FORMAT_WITH_TIME
        } else {
            UI_DATE_FORMAT_WITH_SHORT_TIME
        }
        dateTime.toString(formatter)
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
}

fun Long?.toDateYearFormat(): String? {
    this ?: return null
    if (this == 0L) {
        return null
    }
    return try {
        val dateTime = DateTime(this)
        dateTime.toString(UI_DATE_FORMAT)
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
}

fun Long?.toSpentForTestTimeFormat(): String? {
    this ?: return null
    return try {
        val dateTime = DateTime(this)
        val minute = dateTime.minuteOfHour().get()
        val second = dateTime.secondOfMinute().get()
        return "$minute min $second sec"
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
}

fun Float?.toTimeFormat(): String? {
    this ?: return null
    return try {
        val dateTime = DateTime(this.toLong())
        dateTime.toString(TIME_FORMAT)
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
}

fun Float?.toTeamMemberLastActivityFormat(): String? {
    this ?: return null
    return try {
        val dateTime = DateTime(this.toLong())
        dateTime.toString(TEAM_MEMBER_ACTIVITY_FORMAT)
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
}

fun Float?.toComfortableUiFormat(context: Context): String? {
    this ?: return null
    return this.toLong().toComfortableUiFormat(context)
}

fun Long.toTimerFormat(): String {
    val time = LocalTime(0, 0).plusSeconds(this.toInt())
    return TIMER_SHORT_FORMAT.print(time)
}

fun isSameDays(date1: Float?, date2: Float?): Boolean {
    date1 ?: return false
    date2 ?: return false
    val dateTime1 = DateTime(date1.toLong()).toLocalDate()
    val dateTime2 = DateTime(date2.toLong()).toLocalDate()
    return dateTime1.isEqual(dateTime2)
}

// convert from SERVER_DATE_FORMAT format to age
fun String?.toAge(): Int {
    return try {
        if (this.isNullOrEmpty()) {
            return 0
        }
        val birthdayDateTime = SERVER_DATE_FORMAT
            .parseDateTime(this)
        return getYearsBetween(birthdayDateTime, dateNow)
    } catch (e: Exception) {
        Timber.e(e)
        0
    }
}

fun String?.toYear(): Int {
    return try {
        if (this.isNullOrEmpty()) {
            return 0
        }
        SERVER_DATE_FORMAT
            .parseDateTime(this)
            .year
    } catch (e: Exception) {
        Timber.e(e)
        0
    }
}

//  "2021-09-30" Server date format
val SERVER_DATE_FORMAT_WITH_TIME: DateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss") // 2021-12-06T16:24:35
val SERVER_DATE_FORMAT: DateTimeFormatter = DateTimeFormat.forPattern("YYYY-MM-dd")
//  "07 Sept 2021" UI date format
val UI_DATE_FORMAT: DateTimeFormatter = DateTimeFormat.forPattern("dd.MM.YYYY")
val UI_DATE_FORMAT_WITH_TIME: DateTimeFormatter = DateTimeFormat.forPattern("dd.MM.YYYY, HH:mm:ss")
val UI_DATE_FORMAT_WITH_SHORT_TIME: DateTimeFormatter = DateTimeFormat.forPattern("dd.MM.YYYY, HH:mm")
val TEAM_MEMBER_ACTIVITY_FORMAT: DateTimeFormatter = DateTimeFormat.forPattern("dd.MM.YYYY HH:mm")
val TIMER_SHORT_FORMAT: DateTimeFormatter = DateTimeFormat.forPattern("mm:ss")
val TIME_FORMAT: DateTimeFormatter = DateTimeFormat.forPattern("HH:mm a")
val HH_MM_SS_TIME_LEFT_FORMAT: DateTimeFormatter = DateTimeFormat.forPattern("HH:mm:ss")
val MM_SS_TIME_LEFT_FORMAT: DateTimeFormatter = DateTimeFormat.forPattern("mm:ss")
val SS_TIME_LEFT_FORMAT: DateTimeFormatter = DateTimeFormat.forPattern("ss")
