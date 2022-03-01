package uz.abduvali.pdpuz.fragments

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import uz.abduvali.pdpuz.R
import uz.abduvali.pdpuz.database.AppDatabase
import uz.abduvali.pdpuz.databinding.FragmentAddGroupBinding
import uz.abduvali.pdpuz.entities.Group
import uz.abduvali.pdpuz.entities.Mentor

class AddGroupFragment : Fragment(R.layout.fragment_add_group) {

    private var courseId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            courseId = it.getInt("courseId")
        }
    }

    private val binding by viewBinding(FragmentAddGroupBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val service = AppDatabase.getInstance(requireContext())
        binding.apply {
            back.setOnClickListener { findNavController().popBackStack() }
            val course = service.courseDao().getCourseById(courseId)
            val list = ArrayList<String>()
            val mentorList = ArrayList<Mentor>()
            service.mentorDao().getMentorsWithCourse().forEach {
                if (it.course.id == course.id) {
                    mentorList.add(it.mentor)
                    list.add(it.mentor.firstname)
                }
            }
            mentor.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                list
            )
            date.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                resources.getStringArray(R.array.dates)
            )
            day.adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                resources.getStringArray(R.array.days)
            )
            add.setOnClickListener {
                if (mentorList.isNotEmpty()) {
                    val name = name.text.trim().toString()
                    val mentor = mentor.selectedItem.toString()
                    val date = date.selectedItem.toString()
                    val days = day.selectedItem.toString()
                    if (name.isNotEmpty() && mentor.isNotEmpty() && date.isNotEmpty() && days.isNotEmpty()) {
                        service.groupDao().addGroup(
                            Group(
                                name = name,
                                mentorId = mentorList
                                    .filter { it.firstname == mentor }[0].id,
                                date = date,
                                days = days,
                                isCreated = false
                            )
                        )
                        findNavController().popBackStack()
                    }
                }
            }
        }
    }
}