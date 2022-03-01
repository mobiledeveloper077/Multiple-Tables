package uz.abduvali.pdpuz.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import uz.abduvali.pdpuz.entities.Course
import uz.abduvali.pdpuz.fragments.GroupsPagerFragment

class GroupsPagerAdapter(var fragment: Fragment, var course: Course) :
    FragmentStateAdapter(fragment) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return GroupsPagerFragment.newInstance(course.id, position == 0)
    }
}