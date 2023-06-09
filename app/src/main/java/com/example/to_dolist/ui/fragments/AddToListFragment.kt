package com.example.to_dolist.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.to_dolist.R
import com.example.to_dolist.databinding.FragmentAddToListBinding
import com.example.to_dolist.domain.state.ToDoModelState
import com.example.to_dolist.ui.MainActivityViewModel
import com.example.to_dolist.util.Constants
import com.example.to_dolist.util.ToDoActions
import com.example.to_dolist.util.isExpired
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.StateFlow
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddToListFragment : Fragment() {

    lateinit var binding: FragmentAddToListBinding


    private val viewModel: MainActivityViewModel by viewModels()

    lateinit var state: StateFlow<ToDoModelState>

    lateinit var onEvent: (ToDoActions) -> Unit

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddToListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        state = viewModel.state
        onEvent = viewModel::onToDoAction

        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.tvTitle.text = resources.getText(R.string.add_task_title)
        binding.toolbar.btnOption.visibility = View.GONE


        val shifts = resources.getStringArray(R.array.shifts)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.row_dropdown_item, shifts)

        binding.etTaskShift.setText(shifts[0])
        binding.etTaskShift.setAdapter(arrayAdapter)
        binding.etTaskShift.setOnItemClickListener { parent, view, position, id ->
            Log.e("TAG", "onViewCreated: " + position)
            arrayAdapter.getItem(position)
        }

        binding.toolbar.btnBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnCancel.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnAdd.setOnClickListener {

            val date =
                convertToDate("${binding.etTaskTime.text}" + " ${binding.etTaskShift.text}")

            if (binding.etTaskTitle.text.isNullOrEmpty() || binding.etTaskTime.text.isNullOrEmpty() || binding.etTaskShift.text.isNullOrEmpty()) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.missing_fields),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            } else if (date == null) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.invalid_time),
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            } else {
                onEvent(ToDoActions.SetTitle("${binding.etTaskTitle.text}"))

                onEvent(ToDoActions.SetDate(date))

                onEvent(
                    ToDoActions.SetStatus(if (date.isExpired()) Constants.STATUS_PENDING else Constants.STATUS_CREATED)
                )
                onEvent(ToDoActions.AddToDo)
                Toast.makeText(
                    requireContext(),
                    getString(R.string.to_do_added),
                    Toast.LENGTH_SHORT
                ).show()
                reset()

            }

        }

    }


    fun reset() {
        binding.etTaskTitle.text?.clear()
        binding.etTaskTime.text?.clear()
        binding.etTaskTitle.requestFocus()
    }

    private fun convertToDate(dateString: String?): Date? {
        try {


            val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm a")
            val currentDate = Date()
            val formattedDate = SimpleDateFormat("yyyy-MM-dd").format(currentDate)

            val timeString = "$formattedDate $dateString"
            val dateTime = dateFormat.parse(timeString)

            val outputDateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss 'GMT'Z yyyy")

            val output = outputDateFormat.format(dateTime)

            Log.e("TAG", "convertToDate: " + outputDateFormat.parse(output))
            print(outputDateFormat.parse(output))

            return outputDateFormat.parse(output)


        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

}