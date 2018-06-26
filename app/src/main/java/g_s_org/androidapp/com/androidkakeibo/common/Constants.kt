package g_s_org.androidapp.com.androidkakeibo.common

class Constants{
    companion object {
        //===
        //=== max digits allowed to input
        //===
        const val MAXDIGITS = 8
        //===
        //=== index of type
        //===
        const val INCOME = 0
        const val EXPENSE = 1
        //===
        //=== index of termsOfPayment
        //===
        const val CASH = 0
        const val CARD = 1
        //===
        //=== index of inputTarget
        //===
        const val CATEGORY = 0
        const val DETAIL = 1
        //===
        //=== flag (isSynchronized、isDeleted)
        //===
        const val FALSE = 0
        const val TRUE = 1
        //===
        //=== name of DB
        //===
        const val KAKEIBONAME_MINE = "MyKakeibo"
        //===
        //=== day of week
        //===
        val WEEKNAME = arrayOf("日", "月", "火", "水", "木", "金", "土")
        //===
        //=== max number of weeks in a month
        //===
        const val DAYS_OF_WEEK = 7
        //===
        //=== format of date
        //===
        const val sdf = "yyyy/MM/dd/kk:mm:ss"
        //===
        //=== default year
        //===
        const val DEFAULT_YEAR = 2000
        //===
        //=== condition
        //===
        const val INPUT_TARGET = 0
        const val TERMS_OF_PAYMENT = 1
        const val TYPE = 2
        //===
        //=== current month
        //===
        const val CURRENT_YEAR = 0
        const val CURRENT_MONTH = 1
    }
}