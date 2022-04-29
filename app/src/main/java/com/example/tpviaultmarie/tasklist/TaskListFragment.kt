package com.example.tpviaultmarie.tasklist

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.tpviaultmarie.R
import com.example.tpviaultmarie.form.FormActivity
import com.example.tpviaultmarie.network.Api
import kotlinx.coroutines.launch
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
    private val viewModel: TaskListViewModel by viewModels()

    private val adapter = TaskListAdapter()

    private val createTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as Task? ?: return@registerForActivityResult
        viewModel.create(task)
        //taskList = taskList + task
    }

    private val updateTask = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = result.data?.getSerializableExtra("task") as Task? ?: return@registerForActivityResult
        viewModel.update(task)
        viewModel.refresh()
        //taskList = taskList.map { if (it.id == task.id) task else it }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
        lifecycleScope.launch {
            val userInfo = Api.userWebService.getInfo().body()!!
            val userInfoTextView= view?.findViewById<TextView>(R.id.user)
            userInfoTextView?.text = "${userInfo.firstName} ${userInfo.lastName}"
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
            viewModel.delete(task)
            //taskList = taskList - task
        }
        adapter.onClickUpdate = { task ->
            val intent = Intent(context, FormActivity::class.java)
            intent.putExtra("task", task)
            updateTask.launch(intent)
        }
        lifecycleScope.launch { // on lance une coroutine car `collect` est `suspend`
            viewModel.tasksStateFlow.collect { newList ->
                adapter.submitList(newList);
            }
        }
    }

}