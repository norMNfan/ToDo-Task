package storage

import Utils.Companion.createProps
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private external fun delete(obj: dynamic): Boolean

class Tasks : Storage() {

    private val mutex = Mutex()

    suspend fun add(taskKey: String, translation: String): Boolean {
        mutex.withLock {
            val tasks = getTasks()
            if (contains(tasks, taskKey)) {
                print("[$taskKey] ignored registered word")
                return false
            }
            print("[$taskKey] register new word")
            tasks[taskKey] = createProps("translation", translation)
            setTasks(tasks)
            return true
        }
    }

    suspend fun remove(word: String) {
        mutex.withLock {
            val words = getTasks()
            if (contains(words, word)) {
                delete(words[word])
                setTasks(words)
                print("[$word] removed from words")
            }
        }
    }

    suspend fun getTranslation(wordKey: String) = getTasks()[wordKey].translation as String

    suspend fun changeTranslation(wordKey: String, translation: String) {
        mutex.withLock {
            val words = getTasks()
            words[wordKey].translation = translation
            setTasks(words)
        }
    }

    private fun contains(words: dynamic, word: String) = keys(words).contains(word)
    private fun keys(words: dynamic) = js("Object").keys(words) as Array<String>

    private suspend fun getTasks(): dynamic = getStorage("tasks", js("{}"))

    suspend fun setTasks(words: dynamic) = setStorage("words", words)

    suspend fun getTasksAsArray(filter: (task: dynamic) -> Boolean): Array<dynamic> {
        val tasks = getStorage("tasks", js("{}"))
        return keys(tasks)
            .filter(filter)
            .map {
                createProps("taskKey", it, "description", tasks[it].description)
            }
            .toTypedArray()
    }
}