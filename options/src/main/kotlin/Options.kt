import Utils.Companion.createProps

fun main() {
    setUpdateHandler()
    updatePage()
}

fun setUpdateHandler() {
    chrome.storage.onChanged.addListener { updatePage() }
}

fun updatePage() {
    chrome.runtime.sendMessage(null, createProps("msgType", "getAllData")) { response ->
        TaskList.update(response.tasks)
    }
}