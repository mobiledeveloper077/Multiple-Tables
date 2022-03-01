package uz.abduvali.pdpuz.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import uz.abduvali.pdpuz.R
import uz.abduvali.pdpuz.adapters.CoursesAdapter
import uz.abduvali.pdpuz.database.AppDatabase
import uz.abduvali.pdpuz.databinding.FragmentCoursesBinding
import uz.abduvali.pdpuz.databinding.ItemAddCourseBinding
import uz.abduvali.pdpuz.entities.Course

class CoursesFragment : Fragment(R.layout.fragment_courses) {

    private lateinit var key: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            key = it.getString("key").toString()
        }
    }

    private val binding by viewBinding(FragmentCoursesBinding::bind)
    private lateinit var adapter: CoursesAdapter
    private lateinit var onItemClickListener: CoursesAdapter.onItemClickListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val service = AppDatabase.getInstance(requireContext())
        val list = service.courseDao().getCourseList().toMutableList() as ArrayList
        binding.apply {
            back.setOnClickListener { findNavController().popBackStack() }
            when (key) {
                "courses" -> {
                    onItemClickListener = onClick(R.id.action_coursesFragment_to_courseFragment)
                    add.visibility = View.VISIBLE
                    add.setOnClickListener {
                        val alertDialog = AlertDialog.Builder(requireContext())
                        val itemAddCourseBinding = ItemAddCourseBinding.inflate(layoutInflater)
                        alertDialog.setView(itemAddCourseBinding.root)
                        alertDialog.setCancelable(true)
                        alertDialog.setPositiveButton("Qo'shish") { _, _ ->
                            itemAddCourseBinding.apply {
                                val name = name.text.trim().toString()
                                val description = description.text.trim().toString()
                                if (name.isNotEmpty() && description.isNotEmpty()) {
                                    val id = list[list.size - 1].id + 1
                                    val course = Course(
                                        id = id,
                                        name = name,
                                        description = description
                                    )
                                    service.courseDao().addCourse(course)
                                    list.add(course)
                                    adapter.notifyItemInserted(list.size)
                                }
                            }
                        }
                        alertDialog.setNegativeButton(
                            "Yopish"
                        ) { _, _ -> }
                        alertDialog.create().show()
                    }
                }
                "groups" -> {
                    add.visibility = View.INVISIBLE
                    onItemClickListener = onClick(R.id.action_coursesFragment_to_groupsFragment)
                }
                "mentors" -> {
                    add.visibility = View.INVISIBLE
                    onItemClickListener = onClick(R.id.action_coursesFragment_to_mentorsFragment)
                }
            }

            adapter = CoursesAdapter(list, onItemClickListener)
            rv.adapter = adapter
        }
    }

    private fun onClick(id: Int): CoursesAdapter.onItemClickListener {
        return object : CoursesAdapter.onItemClickListener {
            override fun onClick(course: Course) {
                val bundle = Bundle()
                bundle.putInt("courseId", course.id)
                findNavController().navigate(id, bundle)
            }
        }
    }
}