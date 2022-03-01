package uz.abduvali.pdpuz.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.zhuinden.fragmentviewbindingdelegatekt.viewBinding
import uz.abduvali.pdpuz.R
import uz.abduvali.pdpuz.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding by viewBinding(FragmentHomeBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            pdp.setOnClickListener {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://pdp.uz")
                    )
                )
            }
            courses.setOnClickListener { navigate("courses") }
            groups.setOnClickListener { navigate("groups") }
            mentors.setOnClickListener { navigate("mentors") }
        }
    }

    private fun navigate(key: String) {
        val bundle = Bundle()
        bundle.putString("key", key)
        findNavController().navigate(R.id.action_homeFragment_to_coursesFragment, bundle)
    }
}