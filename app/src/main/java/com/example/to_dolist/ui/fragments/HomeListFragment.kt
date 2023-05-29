package com.example.to_dolist.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.to_dolist.MainActivity
import com.example.to_dolist.R
import com.example.to_dolist.data.model.ToDoModel
import com.example.to_dolist.databinding.FragmentHomeBinding
import com.example.to_dolist.ui.MainActivityViewModel
import com.example.to_dolist.ui.adapters.ToDoListAdapter
import com.example.to_dolist.util.SortType
import com.example.to_dolist.util.ToDoActions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*


@AndroidEntryPoint
class HomeListFragment : Fragment() {


    lateinit var binding: FragmentHomeBinding
    lateinit var adapter: ToDoListAdapter

    private val viewModel: MainActivityViewModel by viewModels()
    lateinit var onEvent: (ToDoActions) -> Unit


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater)
        return binding.root
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.homeToolbar.btnOption.visibility = View.VISIBLE
        binding.homeToolbar.tvTitle.visibility = View.GONE

        onEvent = viewModel::onToDoAction
        adapter = ToDoListAdapter(::markAsCompleteEvent, ::onDelete)

        viewModel.state.onEach {
            if (!binding.rvToDoList.isComputingLayout) {

                if (it.todoList.isEmpty()) {
                    binding.tvEmptyList.visibility = View.VISIBLE
                    binding.rvToDoList.visibility = View.GONE
                } else {
                    binding.tvEmptyList.visibility = View.GONE
                    binding.rvToDoList.visibility = View.VISIBLE
                    adapter.updateListData(it.todoList)
                }
            }
        }.launchIn(viewModel.viewModelScope)


        binding.rvToDoList.adapter = adapter



        binding.btnAddToDo.setOnClickListener {
            findNavController().navigate(R.id.action_homeListFragment_to_addToListFragment)
        }

        binding.homeToolbar.btnOption.setOnClickListener {
            showPopupMenu(it)
        }

        binding.homeToolbar.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

    }


    private fun markAsCompleteEvent(toDoModel: ToDoModel) {
        onEvent(ToDoActions.SetId(toDoModel.id))
        onEvent(ToDoActions.SetTitle(toDoModel.title))
        onEvent(ToDoActions.SetDate(toDoModel.date))
        onEvent(ToDoActions.SetStatus(toDoModel.status))
        onEvent(ToDoActions.AddToDo)
    }

    private fun onDelete(toDoModel: ToDoModel) {
        (activity as MainActivity).showAlert(toDoModel.title) {
            onEvent(ToDoActions.DeleteToDo(toDoModel))
        }
    }

    private fun showPopupMenu(view: View) {
        val popupMenu = PopupMenu(requireContext(), view)
        val inflater: MenuInflater = popupMenu.menuInflater
        inflater.inflate(R.menu.popup_menu, popupMenu.menu)
        popupMenu.setOnMenuItemClickListener(object :
            android.widget.PopupMenu.OnMenuItemClickListener,
            PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {

                return when (item?.itemId) {
                    R.id.sortByDate -> {
                        onEvent(ToDoActions.SortToDoList(SortType.DATE))
                        true
                    }
                    R.id.sortByStatus -> {
                        onEvent(ToDoActions.SortToDoList(SortType.STATUS))
                        true
                    }
                    R.id.sortByTitle -> {
                        onEvent(ToDoActions.SortToDoList(SortType.TITLE))
                        true
                    }
                    else -> false
                }

            }

        })

        popupMenu.show()
    }
}