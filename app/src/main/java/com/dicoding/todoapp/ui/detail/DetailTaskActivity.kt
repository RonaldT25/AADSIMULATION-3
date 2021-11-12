package com.dicoding.todoapp.ui.detail

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.R
import com.dicoding.todoapp.data.Task
import com.dicoding.todoapp.ui.ViewModelFactory
import com.dicoding.todoapp.utils.DateConverter
import com.dicoding.todoapp.utils.TASK_ID
import com.google.android.material.textfield.TextInputEditText

class DetailTaskActivity : AppCompatActivity() {
    private lateinit var detailTaskViewModel: DetailTaskViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        //TODO 11 : Show detail task and implement delete action
        val factory = ViewModelFactory.getInstance(this)
        detailTaskViewModel = ViewModelProvider(this, factory).get(DetailTaskViewModel::class.java)
        val extras = intent.getIntExtra(TASK_ID,-1)
        detailTaskViewModel.setTaskId(extras)
        detailTaskViewModel.task.observe(this, Observer(this::showDetail))

        findViewById<Button>(R.id.btn_delete_task).setOnClickListener { view ->
            detailTaskViewModel.deleteTask()
            super.onBackPressed()
        }
    }

    private fun showDetail(task: Task?) {
        val title : TextInputEditText = findViewById(R.id.detail_ed_title)
        val description : TextInputEditText = findViewById(R.id.detail_ed_description)
        val dueDate : TextInputEditText = findViewById(R.id.detail_ed_due_date)

        if (task != null) {
            title.setText(task.title)
        }
        if (task != null) {
            description.setText(task.description)
        }
        if (task != null) {
            dueDate.setText(DateConverter.convertMillisToString(task.dueDateMillis))
        }
    }

}