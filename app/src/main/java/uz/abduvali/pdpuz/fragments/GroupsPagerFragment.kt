package uz.abduvali.pdpuz.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import uz.abduvali.pdpuz.R
import uz.abduvali.pdpuz.adapters.GroupAdapter
import uz.abduvali.pdpuz.database.AppDatabase
import uz.abduvali.pdpuz.databinding.FragmentGroupsPagerBinding
import uz.abduvali.pdpuz.databinding.ItemEditGroupBinding
import uz.abduvali.pdpuz.entities.Group

class GroupsPagerFragment : Fragment(R.layout.fragment_groups_pager) {

    private var courseId = -1
    private var isCreated: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            courseId = it.getInt("courseId")
            isCreated = it.getBoolean("isCreated")
        }
    }

    private val binding by viewBinding(FragmentGroupsPagerBinding::bind)
    private lateinit var adapter: GroupAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val service = AppDatabase.getInstance(requireContext())
        val mentorList = service.mentorDao().getMentorByCourseId(courseId)
        val list = ArrayList<Group>()
        val studentsCount = ArrayList<Int>()
        mentorList.forEach { list.addAll(service.groupDao().getGroupsByMentorId(it.id)) }
        val groupList = ArrayList<Group>()
        if (isCreated!!) {
            list.forEach {
                if (it.isCreated) {
                    groupList.add(it)
                    studentsCount.add(service.studentDao().getStudentByGroupId(it.id).size)
                }
            }
        } else {
            list.forEach {
                if (!it.isCreated) {
                    groupList.add(it)
                    studentsCount.add(service.studentDao().getStudentByGroupId(it.id).size)
                }
            }
        }
        binding.apply {
            adapter = GroupAdapter(
                groupList,
                studentsCount,
                object : GroupAdapter.OnItemClickListener {
                    override fun itemViewClick(group: Group) {
                        val bundle = Bundle()
                        bundle.putInt("groupId", group.id)
                        findNavController().navigate(
                            R.id.action_groupsFragment_to_groupFragment,
                            bundle
                        )
                    }

                    override fun itemEditClick(group: Group, position: Int) {
                        val alertDialog = AlertDialog.Builder(requireContext())
                        val itemEditMentorBinding = ItemEditGroupBinding.inflate(layoutInflater)
                        alertDialog.setView(itemEditMentorBinding.root)
                        itemEditMentorBinding.apply {
                            name.setText(group.name)
                            val list1 = ArrayList<String>()
                            mentorList.forEach {
                                list1.add(it.firstname)
                            }
                            mentor.adapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_dropdown_item_1line,
                                list1
                            )
                            date.adapter = ArrayAdapter(
                                requireContext(),
                                android.R.layout.simple_dropdown_item_1line,
                                resources.getStringArray(R.array.dates)
                            )
                        }
                        alertDialog.setCancelable(true)
                        alertDialog.setPositiveButton("O'zgartirish") { _, _ ->
                            itemEditMentorBinding.apply {
                                val name = name.text.trim().toString()
                                val mentor = mentor.selectedItem.toString()
                                val date = date.selectedItem.toString()
                                if (name.isNotEmpty() && mentor.isNotEmpty() && date.isNotEmpty()) {
                                    val copy = group.copy(
                                        name = name,
                                        mentorId = mentorList.filter { it.firstname == mentor }[0].id,
                                        date = date
                                    )
                                    service.groupDao().editGroup(copy)
                                    groupList[position] = copy
                                    adapter.notifyItemChanged(position)
                                }
                            }
                        }
                        alertDialog.setNegativeButton(
                            "Yopish"
                        ) { _, _ -> }
                        alertDialog.create().show()
                    }

                    override fun itemDeleteClick(group: Group, position: Int) {
                        val alertDialog = AlertDialog.Builder(requireContext())
                        alertDialog.setCancelable(true)
                        alertDialog.setMessage("Ushbu guruhni o'chirmoqchimisiz ?\nUshbu guruhda o'quvchi barcha talabalar o'chib ketadi.")
                        alertDialog.setPositiveButton("Ha") { _, _ ->
                            service.groupDao().deleteGroup(group)
                            groupList.removeAt(position)
                            adapter.notifyItemRemoved(position)
                            adapter.notifyItemRangeChanged(position, groupList.size)
                        }
                        alertDialog.setNegativeButton(
                            "Yo'q"
                        ) { _, _ -> }
                        alertDialog.create().show()
                    }
                })
            rv.adapter = adapter
        }
        binding.rv.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance(courseId: Int, isCreated: Boolean) =
            GroupsPagerFragment().apply {
                arguments = Bundle().apply {
                    putInt("courseId", courseId)
                    putBoolean("isCreated", isCreated)
                }
            }
    }
}