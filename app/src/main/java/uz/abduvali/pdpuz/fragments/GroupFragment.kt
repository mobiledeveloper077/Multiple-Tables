package uz.abduvali.pdpuz.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import uz.abduvali.pdpuz.R
import uz.abduvali.pdpuz.adapters.StudentAdapter
import uz.abduvali.pdpuz.database.AppDatabase
import uz.abduvali.pdpuz.databinding.FragmentGroupBinding
import uz.abduvali.pdpuz.databinding.ItemEditStudentBinding
import uz.abduvali.pdpuz.entities.Student

class GroupFragment : Fragment(R.layout.fragment_group) {

    private var groupId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            groupId = it.getInt("groupId")
        }
    }

    private val binding by viewBinding(FragmentGroupBinding::bind)
    private lateinit var adapter: StudentAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val service = AppDatabase.getInstance(requireContext())
        val list =
            service.studentDao().getStudentByGroupId(groupId).toMutableList() as ArrayList
        val group = service.groupDao().getGroupById(groupId)
        binding.apply {
            name.text = group.name
            if (group.isCreated) {
                start.visibility = View.INVISIBLE
            } else {
                start.visibility = View.VISIBLE
                if (list.isNotEmpty()) {
                    start.setOnClickListener {
                        service.groupDao().editGroup(group.copy(isCreated = true))
                        findNavController().popBackStack()
                    }
                }
            }
            if (list.isNotEmpty()) {
                description.text =
                    "${group.name}\nO'quvchilar soni: ${list.size} ta\nVaqti: ${group.date}"
                adapter = StudentAdapter(list, object : StudentAdapter.OnItemClickListener {
                    override fun itemEditClick(student: Student, position: Int) {
                        val alertDialog = AlertDialog.Builder(requireContext())
                        val itemEditStudentBinding = ItemEditStudentBinding.inflate(layoutInflater)
                        alertDialog.setView(itemEditStudentBinding.root)
                        alertDialog.setCancelable(true)
                        itemEditStudentBinding.apply {
                            firstname.setText(student.firstname)
                            lastname.setText(student.lastname)
                            middleName.setText(student.middleName)
                        }
                        alertDialog.setPositiveButton("O'zgartirish") { _, _ ->
                            itemEditStudentBinding.apply {
                                val firstname = firstname.text.trim().toString()
                                val lastname = lastname.text.trim().toString()
                                val middleName = middleName.text.trim().toString()
                                if (firstname.isNotEmpty() && lastname.isNotEmpty() && middleName.isNotEmpty()) {
                                    val copy = student.copy(
                                        firstname = firstname,
                                        lastname = lastname,
                                        middleName = middleName
                                    )
                                    service.studentDao().editStudent(copy)
                                    list[position] = copy
                                    adapter.notifyItemChanged(position)
                                }
                            }
                        }
                        alertDialog.setNegativeButton(
                            "Yopish"
                        ) { _, _ -> }
                        alertDialog.create().show()
                    }

                    override fun itemDeleteClick(student: Student, position: Int) {
                        val alertDialog = AlertDialog.Builder(requireContext())
                        alertDialog.setCancelable(true)
                        alertDialog.setTitle("Ushbu o'quvchini o'chirmoqchimisiz ?")
                        alertDialog.setPositiveButton("Ha") { _, _ ->
                            service.studentDao().deleteStudent(student)
                            list.removeAt(position)
                            adapter.notifyItemRemoved(position)
                            adapter.notifyItemRangeChanged(position, list.size)
                            if (list.size == 0) {
                                description.text =
                                    "${group.name}\nO'quvchilar qo'shilmagan\nVaqti: ${group.date}"

                            } else {
                                description.text =
                                    "${group.name}\nO'quvchilar soni: ${list.size} ta\nVaqti: ${group.date}"
                            }
                        }
                        alertDialog.setNegativeButton(
                            "Yo'q"
                        ) { _, _ -> }
                        alertDialog.create().show()
                    }
                })
                rv.adapter = adapter
            } else {
                description.text =
                    "${group.name}\nO'quvchilar qo'shilmagan\nVaqti: ${group.date}"
            }
            back.setOnClickListener { findNavController().popBackStack() }
            add.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt("groupId", group.id)
                findNavController().navigate(
                    R.id.action_groupFragment_to_addStudentGroupFragment,
                    bundle
                )
            }
        }
    }
}