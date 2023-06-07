package com.example.to_dolist.ui.adapters

import android.graphics.Paint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.to_dolist.data.model.ToDoModel
import com.example.to_dolist.databinding.RowToDoItemBinding
import com.example.to_dolist.util.Constants
import com.example.to_dolist.util.Constants.STATUS_COMPLETED
import com.example.to_dolist.util.Constants.STATUS_CREATED
import com.example.to_dolist.util.isExpired
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class ToDoListAdapter(
    val onMarkAsCompleted: (ToDoModel) -> Unit,
    val onDelete: (ToDoModel) -> Unit,
) :
    Adapter<ToDoListAdapter.ToDoViewHolder>() {


    private var todoItems: List<ToDoModel> = emptyList()


    inner class ToDoViewHolder(private val binding: RowToDoItemBinding) :
        ViewHolder(binding.root) {
        fun bind(item: ToDoModel) {

            item.apply {
                binding.tvTaskTitle.text = this.title

                binding.tvTaskTime.text = dateToString(this.date)

                if (this.status != STATUS_COMPLETED && this.date.isExpired()) {
                    binding.tvTaskStatus.visibility = View.VISIBLE
                } else {
                    binding.tvTaskStatus.visibility = View.GONE
                }
                binding.chkMarkComplete.isChecked = this.status == STATUS_COMPLETED
                if (binding.chkMarkComplete.isChecked) {
                    binding.tvTaskTitle.paintFlags =
                        binding.tvTaskTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                } else {
                    binding.tvTaskTitle.paintFlags =
                        binding.tvTaskTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                    binding.tvTaskTitle.paintFlags =
                        binding.tvTaskTitle.paintFlags and Paint.UNDERLINE_TEXT_FLAG.inv()
                }
                binding.chkMarkComplete.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked)
                        this.status = STATUS_COMPLETED
                    else
                        this.status = STATUS_CREATED
                    onMarkAsCompleted(this)
                    notifyItemChanged(position)
                }

                binding.ivDeleteToDo.setOnClickListener {
                    onDelete(this)
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding = RowToDoItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ToDoViewHolder(binding)

    }

    fun updateListData(todoList: List<ToDoModel>) {
        todoItems = todoList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return todoItems.count()
    }

    fun dateToString(date: Date): String {

        val dateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
        val dateTime = dateFormat.parse(date.toString())
        val localFormat = SimpleDateFormat("hh:mm a")
        localFormat.timeZone = TimeZone.getTimeZone("IST")
        val localTime = localFormat.format(dateTime)
        return localTime
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        val item = todoItems[position]
        holder.bind(item)
    }

}