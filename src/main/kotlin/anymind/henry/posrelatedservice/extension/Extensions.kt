package anymind.henry.posrelatedservice.extension

import org.json.JSONArray
import java.util.*

typealias ErrorList = ArrayList<String>

fun JSONArray.contains(value: String): Boolean {
    for (i in 0 until this.length()) {
        if (value == this.get(i)) {
            return true
        }
    }

    return false
}