package uz.abduvali.pdpuz.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import uz.abduvali.pdpuz.R
import uz.abduvali.pdpuz.adapters.MentorAdapter
import uz.abduvali.pdpuz.database.AppDatabase
import uz.abduvali.pdpuz.databinding.FragmentMentorsBinding
import uz.abduvali.pdpuz.databinding.ItemEditMentorBinding
import uz.abduvali.pdpuz.entities.Mentor

class MentorsFragment : Fragment(R.layout.fragment_mentors) {

    private var courseId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            courseId = it.getInt("courseId")
        }
    }

    private val binding by viewBinding(FragmentMentorsBinding::bind)
    private lateinit var adapter: MentorAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val service = AppDatabase.getInstance(requireContext())
        val course = service.courseDao().getCourseById(courseId)
        val list = service.mentorDao().getMentorByCourseId(courseId).toMutableList() as ArrayList
        binding.apply {
            name.text = course.name
            back.setOnClickListener { findNavController().popBackStack() }
            add.setOnClickListener {
                val bundle = Bundle()
                bundle.putInt("courseId", courseId)
                findNavController().navigate(
                    R.id.action_mentorsFragment_to_addMentorFragment,
                    bundle
                )
            }
            if (list.isNotEmpty()) {
                adapter = MentorAdapter(list, object : MentorAdapter.OnItemClickListener {
                    override fun itemEditClick(mentor: Mentor, position: Int) {
                        val alertDialog = AlertDialog.Builder(requireContext())
                        val itemEditMentorBinding = ItemEditMentorBinding.inflate(layoutInflater)
                        alertDialog.setView(itemEditMentorBinding.root)
                        itemEditMentorBinding.apply {
                            firstname.setText(mentor.firstname)
                            lastname.setText(mentor.lastname)
                            middleName.setText(mentor.middleName)
                        }
                        alertDialog.setCancelable(true)
                        alertDialog.setPositiveButton("O'zgartirish") { _, _ ->
                            itemEditMentorBinding.apply {
                                val firstname = firstname.text.trim().toString()
                                val lastname = lastname.text.trim().toString()
                                val middleName = middleName.text.trim().toString()
                                if (firstname.isNotEmpty() && lastname.isNotEmpty() && middleName.isNotEmpty()) {
                                    val copy = mentor.copy(
                                        firstname = firstname,
                                        lastname = lastname,
                                        middleName = middleName
                                    )
                                    service.mentorDao().editMentor(copy)
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

                    override fun itemDeleteClick(mentor: Mentor, position: Int) {
                        val alertDialog = AlertDialog.Builder(requireContext())
                        alertDialog.setCancelable(true)
                        alertDialog.setMessage("Ushbu mentorni o'chirmoqchimisiz ?\nUshbu mentor dars o'tadigan barcha guruhlar va talabalar o'chib ketadi.")
                        alertDialog.setPositiveButton("Ha") { _, _ ->
                            service.mentorDao().deleteMentor(mentor)
                            list.removeAt(position)
                            adapter.notifyItemRemoved(position)
                            adapter.notifyItemRangeChanged(position, list.size)
                        }
                        alertDialog.setNegativeButton(
                            "Yo'q"
                        ) { _, _ -> }
                        alertDialog.create().show()
                    }
                })
                rv.adapter = adapter
            }
        }
    }
}