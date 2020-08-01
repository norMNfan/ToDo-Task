import Utils.Companion.createProps
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import storage.Tasks

class Background {

    private val tasks = Tasks()

    init {
        setMessageHandler()
    }

    private fun setMessageHandler() {
        chrome.runtime.onMessage.addListener { request, _, response ->
            GlobalScope.launch {
                println("GOT REQUEST")
                when (request.msgType) {
                    "addTask" -> addTask(request)
                    "getAllData" -> getAllData(response)
                }
            }
            js("return true")
        }
    }

    private suspend fun addTask(request: dynamic) {
        println("Adding task: ${request.taskKey} : ${request.description}")
        if (tasks.add(request.taskKey, request.description)) {
//            chrome.notifications.create(
//                "addTask.${request.description}", createProps(
//                    "type", "basic",
//                    "iconUrl", "icon128.png",
//                    "title", "New word registered",
//                    "buttons", arrayOf(createProps("title", "Cancel word registration"))
//                )
//            )
        }
    }

    private suspend fun getAllData(response: dynamic) {
        println("getting all data...")
        val tasks = tasks.getTasksAsArray { true }
        response(createProps("tasks", tasks))
    }
}