package com.averin.android.developer.base.util

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.DateTimeZone.UTC
import org.joda.time.LocalDate
import org.joda.time.LocalTime
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

fun String?.toUIFormat(): String? {
    return try {
        if (this.isNullOrEmpty()) {
            return null
        }
        return convertFormats(SERVER_DATE_FORMAT, UI_DATE_FORMAT)
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
}

fun DateTime.toUIFormat(
    toFormat: DateTimeFormatter = UI_DATE_FORMAT
): String? {
    return try {
        toString(toFormat)
    } catch (e: Exception) {
        Timber.e(e)
        null
    }
}

fun String?.toServerFormat(): String? {
    return try {
        if (this.isNullOrEmpty()) {
            return null
        }
        return convertFormats(UI_DATE_FORMAT, SERVER_DATE_FORMAT)
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

fun Long.toTimerFormat(): String {
    val time = LocalTime(0, 0).plusSeconds(this.toInt())
    return LEFT_TIMER_FORMAT.print(time)
}

//  "2016-03-24T14:22:28Z" Server date format
val SERVER_DATE_FORMAT: DateTimeFormatter = DateTimeFormat.forPattern("YYYY-MM-ddTHH:mm:ssZ")
//  "07 Sept 2021" UI date format
val UI_DATE_FORMAT: DateTimeFormatter = DateTimeFormat.forPattern("dd.MM.YYYY")

//  "02:23" left timer format
val LEFT_TIMER_FORMAT: DateTimeFormatter = DateTimeFormat.forPattern("mm:ss")
