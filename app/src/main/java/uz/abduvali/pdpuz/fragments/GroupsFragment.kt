package uz.abduvali.pdpuz.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import uz.abduvali.pdpuz.R
import uz.abduvali.pdpuz.adapters.GroupsPagerAdapter
import uz.abduvali.pdpuz.database.AppDatabase
import uz.abduvali.pdpuz.databinding.FragmentGroupsBinding

class GroupsFragment : Fragment(R.layout.fragment_groups) {

    private var courseId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            courseId = it.getInt("courseId")
        }
    }

    private val binding by viewBinding(FragmentGroupsBinding::bind)
    private lateinit var adapter: GroupsPagerAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val course = AppDatabase.getInstance(requireContext()).courseDao().getCourseById(courseId)
        binding.apply {
            name.text = course.name
            back.setOnClickListener { findNavController().popBackStack() }
            adapter = GroupsPagerAdapter(this@GroupsFragment, course)
            pager.adapter = adapter
            TabLayoutMediator(
                tabLayout,
                pager
            ) { tab, position ->
                if (position == 0) {
                    tab.text = "Ochilgan guruhlar"
                    add.visibility = View.INVISIBLE
                } else {
                    tab.text = "Ochilayotgan guruhlar"
                    add.visibility = View.VISIBLE
                    add.setOnClickListener {
                        val bundle = Bundle()
                        bundle.putInt("courseId", course.id)
                        findNavController().navigate(
                            R.id.action_groupsFragment_to_addGroupFragment,
                            bundle
                        )
                    }
                }
            }.attach()
        }
    }
}