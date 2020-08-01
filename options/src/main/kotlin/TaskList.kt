import Utils.Companion.createProps
import kotlinx.html.*
import kotlinx.html.dom.create
import org.w3c.dom.HTMLDivElement
import kotlin.browser.document
import Utils.Companion.select
import kotlinx.html.js.onClickFunction

class TaskList {

    companion object {
        fun update(tasks: Array<dynamic>) {
            println("updating... $tasks")
            updateTaskList(tasks)
        }

        private fun updateTaskList(tasks: Array<dynamic>) {
            println(tasks)
            select<HTMLDivElement>("#tasks").replaceWith(document.create.table {
                id = "tasks"
                classes = setOf("table", "table-striped", "table-hover", "table-sm")
                thead("thead-dark") {
                    tr {
                        th(scope = ThScope.col) { +"" }
                        th(scope = ThScope.col) { +"Task" }
                    }
                }
                tbody {
                    for (task in tasks) {
                        tr {
                            th(scope = ThScope.row) {  }
                            td { +(task.description) }
                        }
                    }
                    tr {
                        td {
                            button(classes = "btn btn-sm btn-outline-dark") {
                                style = "outline:none;"
                                i("fa fa-plus")
                                title = "addTask".replace("([A-Z])".toRegex(), " $1").toLowerCase()
                                onClickFunction = {
                                    println("CLICKED")
                                    chrome.runtime.sendMessage(
                                        null,
                                        createProps("msgType", "addTask", "taskKey", "word.wordKey")
                                    )
                                }
                            }
                        }
                        td {
                            input {
                                type = InputType.text
                                name = "TaskInput"
                            }
                        }
                    }
                }
            })
        }
    }
}