package br.com.kelvingcr.task.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.kelvingcr.task.R
import br.com.kelvingcr.task.databinding.FragmentHomeBinding
import br.com.kelvingcr.task.databinding.FragmentTodoBinding
import br.com.kelvingcr.task.model.Task
import br.com.kelvingcr.task.ui.HomeFragmentDirections
import br.com.kelvingcr.task.ui.adapter.TaskAdapter
import br.com.kelvingcr.task.ui.helper.FirebaseHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


class TodoFragment : Fragment() {

    private var _binding: FragmentTodoBinding? = null
    private val binding get() = _binding!!

    private lateinit var taskAdapter: TaskAdapter

    private val taskList = mutableListOf<Task>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initClicks()
        getTasks()
    }

    private fun initClicks() {
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_formTaskFragment)

        }
    }

    private fun getTasks() {
        FirebaseHelper
            .getDatabase()
            .child("task")
            .child(FirebaseHelper.getIdUser() ?: "")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        taskList.clear()
                        for (snap in snapshot.children) {
                            val task = snap.getValue(Task::class.java) as Task

                            if (task.status == 0) taskList.add(task)
                        }

                        taskList.reverse()
                        initAdapter()
                    }

                    tasksEmpty()

                    binding.progressBar.isVisible = false
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Erro", Toast.LENGTH_SHORT).show()
                }

            })
    }

    private fun tasksEmpty() {
        binding.textInfo.text = if (taskList.isEmpty()) {
            getText(R.string.text_task_list_empty_todo_fragment)
        } else {
            ""
        }
    }

    private fun initAdapter() {
        binding.rvTask.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTask.setHasFixedSize(true)
        taskAdapter = TaskAdapter(requireContext(), taskList) { task, select ->
            optionSelect(task, select)
        }
        binding.rvTask.adapter = taskAdapter
    }

    private fun optionSelect(task: Task, select: Int) {
        when (select) {
            TaskAdapter.SELECT_REMOVE -> {
                deleteTask(task)
            }
            TaskAdapter.SELECT_EDIT -> {
                 val action = HomeFragmentDirections
                      .actionHomeFragmentToFormTaskFragment(task)
                  findNavController().navigate(action)
            }
            TaskAdapter.SELECT_NEXT -> {
                task.status = 1
                // updateTask(task)
            }
        }
    }

    private fun deleteTask(task: Task) {

        FirebaseHelper
            .getDatabase()
            .child("task")
            .child(FirebaseHelper.getIdUser() ?: "")
            .child(task.id)
            .removeValue()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        R.string.text_task_update_sucess,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // showBottomSheet(message = R.string.error_generic)
                }
            }.addOnFailureListener {
                binding.progressBar.isVisible = false
                // showBottomSheet(message = R.string.error_generic)
            }

        taskList.remove(task)
        taskAdapter.notifyDataSetChanged()

        Toast.makeText(requireContext(), R.string.text_task_delete_sucess, Toast.LENGTH_SHORT)
            .show()
    }

    private fun updateTask(task: Task) {
        FirebaseHelper
            .getDatabase()
            .child("task")
            .child(FirebaseHelper.getIdUser() ?: "")
            .child(task.id)
            .setValue(task)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        R.string.text_task_update_sucess,
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                  //  showBottomSheet(message = R.string.error_generic)
                }
            }.addOnFailureListener {
                binding.progressBar.isVisible = false
               // showBottomSheet(message = R.string.error_generic)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}