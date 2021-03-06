package g_s_org.androidapp.com.androidkakeibo.view

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.widget.Button

import g_s_org.androidapp.com.androidkakeibo.R
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import g_s_org.androidapp.com.androidkakeibo.common.Constants
import java.util.*

class CalendarDialogFragment : DialogFragment() {
    // activity which call this fragment
    lateinit var mCaller: Activity
    // date when dialog is shown
    private var originalYear = Constants.DEFAULT_YEAR
    private var originalMonth = 1
    private var originalDay = 1
    // current selected month
    private var currentYear = Constants.DEFAULT_YEAR
    private var currentMonth = 1

    //===
    //=== life cycle
    //===
    override fun onAttach(context: Context?) {
        mCaller = context as Activity
        super.onAttach(context as Context)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // initialize values
        initValues()
        // create view to display in dialog
        val calendarView = View.inflate(mCaller, R.layout.fragment_calendar_dialog, null)
        // fill value and listener of calendar view
        setCalendarView(calendarView, currentYear, currentMonth)
        // create dialog
        val dialog = AlertDialog.Builder(mCaller)
                .setView(calendarView)
                .setNegativeButton(R.string.bt_cancel, null)
                .create()
        // display no title
        dialog.requestWindowFeature(DialogFragment.STYLE_NO_FRAME)
        return dialog
    }

    //===
    //=== initialize values
    //===
    fun initValues() {
        // get arguments
        originalYear = arguments.getInt("ORIGINAL_YEAR")
        originalMonth = arguments.getInt("ORIGINAL_MONTH")
        originalDay = arguments.getInt("ORIGINAL_DAY")
        // set current selected month
        currentYear = originalYear
        currentMonth = originalMonth
    }

    //===
    //=== view setter
    //===
    fun setCalendarView(view: View, year: Int, month: Int) {
        // set year and month text
        (view.findViewById(R.id.tv_yearAndMonth_calendar) as TextView).text = getString(R.string.show_yearandmonth, year, month)
        // set listener to previous month button and next month button
        (view.findViewById(R.id.bt_previous_calendar) as Button).setOnClickListener { onPreviousMonthClicked(view) }
        (view.findViewById(R.id.bt_next_calendar) as Button).setOnClickListener { onNextMonthClicked(view) }
        // get first day of the month
        val firstDay = Calendar.getInstance()
        firstDay.set(year, month - 1, 1)
        // fill the calendar with days
        setMonth(view.findViewById(R.id.tl_days) as TableLayout, 1, 1,
                firstDay.get(Calendar.DAY_OF_WEEK), firstDay.getActualMaximum(Calendar.DAY_OF_MONTH))
    }

    //===
    //=== listener
    //===
    fun onPreviousMonthClicked(view: View) {
        when (currentMonth) {
            1 -> {
                // back to December in the previous year
                currentYear--
                currentMonth = 12
            }
            else -> {
                currentMonth--
            }
        }
        // set the previous month to calendar
        setCalendarView(view, currentYear, currentMonth)
    }

    fun onNextMonthClicked(view: View) {
        when (currentMonth) {
            12 -> {
                // proceed to January in the next year
                currentYear++
                currentMonth = 1
            }
            else -> {
                currentMonth++
            }
        }
        // set the next month to calendar
        setCalendarView(view, currentYear, currentMonth)
    }

    fun onDateClicked(day: Int) {
        // get parent fragment
        val parent = parentFragment
        if (parent is OnDialogInteractionListener) {
            // callback
            parent.onDialogDateSelected(currentYear, currentMonth, day)
            // close dialog
            dismiss()
        } else {
            throw UnsupportedOperationException("listener is not implemented")
        }
    }

    interface OnDialogInteractionListener {
        fun onDialogDateSelected(year: Int, month: Int, day: Int)
    }

    //===
    //=== business logic
    //===
    // set week to the table(month)
    private tailrec fun setMonth(tl: TableLayout, weekOfMonth: Int, day: Int, firstDayOfWeek: Int, lastDay: Int) {
        when {
            (weekOfMonth >= tl.childCount) -> {
                // after last week
                return
            }
            (weekOfMonth == 1) -> {
                // first week of month
                setWeek(tl.getChildAt(weekOfMonth) as TableRow, 0, day, firstDayOfWeek - 1, lastDay)
                return setMonth(tl, weekOfMonth + 1, day + Constants.DAYS_OF_WEEK - firstDayOfWeek + 1, 0, lastDay)
            }
            else -> {
                // middle weeks of month
                setWeek(tl.getChildAt(weekOfMonth) as TableRow, 0, day, 0, lastDay)
                return setMonth(tl, weekOfMonth + 1, day + Constants.DAYS_OF_WEEK, 0, lastDay)
            }
        }
    }

    // set day to the table row(week)
    private tailrec fun setWeek(tr: TableRow, col: Int, day: Int, firstCol: Int, lastDay: Int) {
        when {
            (col >= tr.childCount) -> {
                return
            }
            (col < firstCol || day > lastDay) -> {
                // fill empty cell before the first day or after the last day
                setEmpty(tr.getChildAt(col) as TextView)
                return setWeek(tr, col + 1, day, firstCol, lastDay)
            }
            else -> {
                setDay(tr.getChildAt(col) as TextView, day)
                return setWeek(tr, col + 1, day + 1, firstCol, lastDay)
            }
        }
    }

    // set day and the corresponding listener to text view (change background if the original day)
    private fun setDay(t: TextView, d: Int) {
        t.text = d.toString()
        t.setOnClickListener { onDateClicked(d) }
        if (currentYear == originalYear && currentMonth == originalMonth && d == originalDay) {
            t.setBackgroundResource(R.drawable.calendar_today)
        } else {
            t.setBackgroundResource(R.drawable.buttons)
        }
    }

    // set empty string and no listener to textView
    private fun setEmpty(t: TextView) {
        t.text = ""
        t.setOnClickListener(null)
        t.setBackgroundResource(R.drawable.buttons)
    }

    //===
    //=== factory method
    //===
    companion object {
        fun newInstance(y: Int, m: Int, d: Int): CalendarDialogFragment {
            // fragment
            val fragment = CalendarDialogFragment()
            // set year, month, day when "date" is tapped
            val args = Bundle()
            args.putInt("ORIGINAL_YEAR", y)
            args.putInt("ORIGINAL_MONTH", m)
            args.putInt("ORIGINAL_DAY", d)
            // pass arguments to fragment
            fragment.arguments = args
            // prohibit cancel with "return" button
            fragment.isCancelable = false
            return fragment
        }
    }
}
