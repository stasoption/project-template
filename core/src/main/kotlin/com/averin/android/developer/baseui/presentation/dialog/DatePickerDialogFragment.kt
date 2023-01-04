package com.averin.android.developer.baseui.presentation.dialog

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.averin.android.developer.base.util.dateNow
import com.averin.android.developer.baseui.util.delegates.RequiredArgument
import org.joda.time.DateTime

class DatePickerDialogFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {

    private var minDate: Long by RequiredArgument
    private var maxDate: Long by RequiredArgument
    private var initialDate: DateTime by RequiredArgument
    private var onDataSelectListener: ((DateTime) -> Unit)? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = DatePickerDialog(
            requireContext(),
            this@DatePickerDialogFragment,
            initialDate.year,
            initialDate.monthOfYear - 1,
            initialDate.dayOfMonth
        )
        if (minDate > 0) {
            dialog.datePicker.minDate = minDate
        }
        if (maxDate > 0) {
            dialog.datePicker.maxDate = maxDate
        }
        // remove title from the dialog
        dialog.setTitle(null)
        return dialog
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        onDataSelectListener?.invoke(DateTime(year, month + 1, day, 0, 0))
    }

    interface Callback {
        fun onDateSet(year: Int, month: Int, day: Int)
    }

    companion object {
        @JvmOverloads
        fun show(
            fm: FragmentManager,
            dialogInitialDate: DateTime = dateNow,
            dialogMinDate: Long = dateNow.millis,
            dialogMaxDate: Long = 0,
            listener: ((DateTime) -> Unit)? = null,
            tag: String = DatePickerDialogFragment::class.java.name
        ) {
            DatePickerDialogFragment().apply {
                initialDate = dialogInitialDate
                minDate = if (dialogMinDate < 0) 0 else dialogMinDate
                maxDate = if (dialogMaxDate < 0) 0 else dialogMaxDate
                onDataSelectListener = listener
                show(fm, tag)
            }
        }
    }
}
