package g_s_org.androidapp.com.androidkakeibo.model

import java.util.*

//===
//=== get priceString of priceStack
//===
fun Deque<Char>.getPrice(): String {
    // string builder
    val priceString = StringBuilder()
    // temporary deque
    val tempStack = ArrayDeque(this)

    for (i in this.indices) {
        if (i != 0 && i % 3 == 0) {
            // insert ',' every three digits
            priceString.append(',')
        }
        priceString.append(tempStack.removeLast())
    }
    // add '￥' at the end
    priceString.append('￥')
    // reverse order
    priceString.reverse()
    return priceString.toString()
}

//===
//=== store price to stack
//===
fun Deque<Char>.setPrice(price: Int) {
    // change price to type array of char
    val priceArray = price.toString().toCharArray()
    // initialize deque
    this.clear()
    // push array to deque
    for (ch in priceArray) {
        this.addLast(ch)
    }
}
