import kotlin.browser.document

external val chrome: dynamic

class Utils {
    companion object {
        fun createProps(vararg keyVal: dynamic): dynamic {
            val props = js("{}")
            for (i in 0 until keyVal.size / 2) {
                props[keyVal[i * 2]] = keyVal[i * 2 + 1]
            }
            return props
        }

        fun <T> select(selector: String): T {
            @Suppress("UNCHECKED_CAST")
            return document.querySelector(selector) as T
        }
    }
}