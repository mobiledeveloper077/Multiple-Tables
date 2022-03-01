package uz.abduvali.pdpuz.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import uz.abduvali.pdpuz.R
import uz.abduvali.pdpuz.database.AppDatabase
import uz.abduvali.pdpuz.databinding.FragmentAddStudentBinding
import uz.abduvali.pdpuz.entities.Group
import uz.abduvali.pdpuz.entities.Student
import java.util.*
import kotlin.collections.ArrayList

class AddStudentFragment : Fragment(R.layout.fragment_add_student) {

    private var courseId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            courseId = it.getInt("courseId")
        }
    }

    private val binding by viewBinding(FragmentAddStudentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val service = AppDatabase.getInstance(requireContext())
        val course = service.courseDao().getCourseById(courseId)
        binding.apply {
            back.setOnClickListener { findNavController().popBackStack() }
            date.setOnClickListener {
                val calendar = Calendar.getInstance()
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val month = calendar.get(Calendar.MONTH) + 1
                val year = calendar.get(Calendar.YEAR)
                DatePickerDialog(
                    requireContext(),
                    { _, p1, p2, p3 -> date.setText("$p3.$p2.$p1") },
                    year,
                    month,
                    day
                ).show()
            }
            val mentorList = service.mentorDao().getMentorByCourseId(course.id)
            val groupList = ArrayList<Group>()
            mentorList.forEach {
                groupList.addAll(service.groupDao().getGroupsByMentorId(it.id))
            }
            val list1 = ArrayList<String>()
            val list2 = ArrayList<String>()
            mentorList.forEach { list1.add(it.firstname) }
            groupList.forEach { list2.add(it.name) }
            mentor.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                list1
            )
            group.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                list2
            )
            save.setOnClickListener {
                if (mentorList.isNotEmpty() && groupList.isNotEmpty()) {
                    val lastname = lastname.text.toString()
                    val firstname = firstname.text.toString()
                    val middleName = middleName.text.toString()
                    val date = date.text.toString()
                    val mentor = mentor.selectedItem.toString()
                    val group = group.selectedItem.toString()
                    if (lastname.isNotEmpty() && firstname.isNotEmpty() && middleName.isNotEmpty() && date.isNotEmpty() && mentor.isNotEmpty() && group.isNotEmpty()) {
                        service.studentDao().addStudent(
                            Student(
                                lastname = lastname,
                                firstname = firstname,
                                middleName = middleName,
                                date = date,
                                groupId = groupList.filter { it.name == group }[0].id
                            )
                        )
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }
}