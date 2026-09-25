package machine7y.grayforce.presentation.utils

import android.content.Context
import machine7y.grayforce.R

private const val SECOND_IN_HOUR = 3600
private const val SECOND_IN_MINUTE = 60
private const val MIDDLE = 300

fun Int.scaleValue(): Int = if (this > MIDDLE) {
    MIDDLE + (this - MIDDLE) * 60f
} else {
    this
}.toInt()

fun Int.unscaleValue(): Int = if (this > MIDDLE) {
    MIDDLE + (this - MIDDLE) / 60f
} else {
    this
}.toInt()

fun Int.toDurationString(context: Context): String {
    val hours = this / SECOND_IN_HOUR
    val minutes = (this % SECOND_IN_HOUR) / SECOND_IN_MINUTE
    val seconds = this % SECOND_IN_MINUTE

    return buildString {
        if (hours > 0) append(context.getString(R.string.duration_hours, hours))
        if (minutes > 0) append(context.getString(R.string.duration_minutes, minutes))
        if (seconds > 0) append(context.getString(R.string.duration_seconds, seconds))
        if (isEmpty()) append(context.getString(R.string.duration_seconds, 0))
    }
}
