package uz.abduvali.pdpuz.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import uz.abduvali.pdpuz.R
import uz.abduvali.pdpuz.database.AppDatabase
import uz.abduvali.pdpuz.databinding.FragmentCourseBinding

class CourseFragment : Fragment(R.layout.fragment_course) {

    private var courseId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            courseId = it.getInt("courseId")
        }
    }

    private val binding by viewBinding(FragmentCourseBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val course = AppDatabase.getInstance(requireContext()).courseDao().getCourseById(courseId)
        binding.apply {
            name.text = course.name
            description.text = course.description
            addStudent.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt("courseId", courseId)
                findNavController().navigate(
                    R.id.action_courseFragment_to_addStudentFragment,
                    bundle
                )
            }
            back.setOnClickListener { findNavController().popBackStack() }
        }
    }
}