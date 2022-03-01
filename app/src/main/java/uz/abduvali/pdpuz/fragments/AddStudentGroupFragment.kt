package uz.abduvali.pdpuz.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import uz.abduvali.pdpuz.R
import uz.abduvali.pdpuz.database.AppDatabase
import uz.abduvali.pdpuz.databinding.FragmentAddStudentGroupBinding
import uz.abduvali.pdpuz.entities.Student
import java.util.*

class AddStudentGroupFragment : Fragment(R.layout.fragment_add_student_group) {

    private var groupId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            groupId = it.getInt("groupId")
        }
    }

    private val binding by viewBinding(FragmentAddStudentGroupBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val service = AppDatabase.getInstance(requireContext())
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
            save.setOnClickListener {
                val lastname = lastname.text.trim().toString()
                val firstname = firstname.text.trim().toString()
                val middleName = middleName.text.trim().toString()
                val date = date.text.toString()
                if (lastname.isNotEmpty() && firstname.isNotEmpty() && middleName.isNotEmpty() && date.isNotEmpty()) {
                    service.studentDao().addStudent(
                        Student(
                            lastname = lastname,
                            firstname = firstname,
                            middleName = middleName,
                            date = date,
                            groupId = groupId
                        )
                    )
                    findNavController().popBackStack()
                }
            }
        }
    }
}