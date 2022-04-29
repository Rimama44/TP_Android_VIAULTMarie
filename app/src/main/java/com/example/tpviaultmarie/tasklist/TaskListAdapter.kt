package com.example.tpviaultmarie.tasklist

import android.app.ActivityManager
import android.content.ClipData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.tpviaultmarie.R

object ItemsDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task) : Boolean {
        return oldItem.id == newItem.id// comparison: are they the same "entity" ? (usually same id)
    }

    override fun areContentsTheSame(oldItem: Task, newItem: Task) : Boolean {
        return oldItem == newItem// comparison: are they the same "content" ? (simplified for data class)
    }
}

// l'IDE va râler ici car on a pas encore implémenté les méthodes nécessaires
class TaskListAdapter : androidx.recyclerview.widget.ListAdapter<Task, TaskListAdapter.TaskViewHolder>(ItemsDiffCallback) {

    var onClickDelete: (Task) -> Unit = {}
    var onClickUpdate: (Task) -> Unit = {}

    // on utilise `inner` ici afin d'avoir accès aux propriétés de l'adapter directement
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(task: Task,) {
            val titleView = itemView.findViewById<TextView>(R.id.task_title)
            titleView.text = task.title
            val descriptionView = itemView.findViewById<TextView>(R.id.task_description)
            descriptionView.text = task.description
            val deleteButton = itemView.findViewById<ImageButton>(R.id.floatingActionButton2)
            deleteButton.setOnClickListener{ onClickDelete(task) }
            val updateButton = itemView.findViewById<ImageButton>(R.id.update_imagebutton)
            updateButton.setOnClickListener{ onClickUpdate(task) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

}