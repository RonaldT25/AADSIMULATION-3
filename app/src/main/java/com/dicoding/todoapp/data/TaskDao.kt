package com.dicoding.todoapp.data

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import java.util.*

//TODO 2 : Define data access object (DAO)
@Dao
interface TaskDao {
    @RawQuery(observedEntities = [Task::class])
    fun getTasks(query: SupportSQLiteQuery): DataSource.Factory<Int, Task>
    @Query("SELECT * FROM Task WHERE id IN (:taskId)")
    fun getTaskById(taskId: Int): LiveData<Task>
    @Query("SELECT * FROM Task WHERE completed=0 AND dueDate>=(:now) ORDER BY dueDate ASC LIMIT 1 ")
    fun getNearestActiveTask(now:Long = Calendar.getInstance().timeInMillis): Task
    @Insert
    suspend fun insertTask(task: Task): Long
    @Insert
    fun insertAll(vararg tasks: Task)
    @Delete
    suspend fun deleteTask(task: Task)
    @Query("UPDATE Task SET completed=:completed WHERE id = :taskId")
    suspend fun updateCompleted(taskId: Int, completed: Boolean)
}
