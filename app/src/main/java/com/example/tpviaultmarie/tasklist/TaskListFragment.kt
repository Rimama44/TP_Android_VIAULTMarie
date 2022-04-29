package com.example.tpviaultmarie.tasklist

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import com.example.tpviaultmarie.R
import com.example.tpviaultmarie.form.FormActivity
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [TaskListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class TaskListFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )
    private val adapter = TaskListAdapter()

    private val createTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as Task? ?: return@registerForActivityResult
        taskList = taskList + task
        refreshAdapter()
    }

    private val updateTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as Task? ?: return@registerForActivityResult
        taskList = taskList.map { if (it.id == task.id) task else it }
        refreshAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        adapter.submitList(taskList);
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_task_list, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.adapter = this.adapter
        val addButton = view.findViewById<ImageButton>(R.id.floatingActionButton)
        addButton.setOnClickListener(){
            val intent = Intent(context, FormActivity::class.java)
            createTask.launch(intent)
        }
        adapter.onClickDelete = { task ->
            taskList = taskList - task
            refreshAdapter()
        }
        adapter.onClickUpdate = { task ->
            val intent = Intent(context, FormActivity::class.java)
            intent.putExtra("task", task)
            updateTask.launch(intent)
        }
    }

    private fun refreshAdapter(){
        adapter.submitList(taskList);
    }
}