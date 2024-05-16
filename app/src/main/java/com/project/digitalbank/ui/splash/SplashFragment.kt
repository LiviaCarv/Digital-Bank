package com.project.digitalbank.ui.splash


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.project.digitalbank.R
import com.project.digitalbank.databinding.FragmentSplashBinding
import com.project.digitalbank.util.FirebaseHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed(this::checkAuthentication, 3000)
    }

    private fun checkAuthentication() {

        val action = if (FirebaseHelper.isAuthenticated()) {
            R.id.action_global_homeFragment
        } else {
            R.id.action_global_authentication
        }
        val navOptions: NavOptions = NavOptions.Builder().setPopUpTo(R.id.splashFragment,true).build()
        findNavController().navigate(action, null, navOptions)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}