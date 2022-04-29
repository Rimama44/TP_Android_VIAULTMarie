package com.example.tpviaultmarie.form

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import com.example.tpviaultmarie.R
import com.example.tpviaultmarie.tasklist.Task
import java.util.*

class FormActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        val createdTitle = findViewById<EditText>(R.id.task_title);
        val createdDescription = findViewById<EditText>(R.id.task_descritpion);

        val taskUpdate = intent.getSerializableExtra("task") as? Task
        createdTitle.setText(taskUpdate?.title)
        createdDescription.setText(taskUpdate?.description)

        val createButton = findViewById<ImageButton>(R.id.create_imagebutton)

        createButton.setOnClickListener() {
            val newTask = Task(id = taskUpdate?.id ?: UUID.randomUUID().toString(), title = createdTitle.text.toString(), description = createdDescription.text.toString())
            intent.putExtra("task", newTask)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

}