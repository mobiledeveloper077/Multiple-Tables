package uz.abduvali.pdpuz.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import uz.abduvali.pdpuz.R
import uz.abduvali.pdpuz.database.AppDatabase
import uz.abduvali.pdpuz.databinding.FragmentAddMentorBinding
import uz.abduvali.pdpuz.entities.Mentor

class AddMentorFragment : Fragment(R.layout.fragment_add_mentor) {

    private var courseId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            courseId = it.getInt("courseId")
        }
    }

    private val binding by viewBinding(FragmentAddMentorBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val service = AppDatabase.getInstance(requireContext())
        val course = service.courseDao().getCourseById(courseId)
        binding.apply {
            back.setOnClickListener { findNavController().popBackStack() }
            add.setOnClickListener {
                val firstname = firstname.text.trim().toString()
                val lastname = lastname.text.trim().toString()
                val middleName = middleName.text.trim().toString()
                if (firstname.isNotEmpty() && lastname.isNotEmpty() && middleName.isNotEmpty()) {
                    service.mentorDao().addMentor(
                        Mentor(
                            courseId = course.id,
                            firstname = firstname,
                            lastname = lastname,
                            middleName = middleName
                        )
                    )
                    findNavController().popBackStack()
                }
            }
        }
    }
}