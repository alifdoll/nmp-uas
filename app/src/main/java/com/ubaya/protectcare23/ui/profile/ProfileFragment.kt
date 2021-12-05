package com.ubaya.protectcare23.ui.profile

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.ubaya.protectcare23.R
import com.ubaya.protectcare23.api.ApiService
import com.ubaya.protectcare23.databinding.FragmentProfileBinding
import com.ubaya.protectcare23.di.Injection
import com.ubaya.protectcare23.ui.auth.AuthActivity
import com.ubaya.protectcare23.utils.Global
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        apiService = Injection.provideService(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            val token = requireActivity().getSharedPreferences("token", Context.MODE_PRIVATE)
                .getString("token","").toString()
            val user = apiService.getUser(token)
            binding.txtName.setText(user.name)
            binding.txtVaksin.setText(user.doses.toString())
        }

        binding.fab.setOnClickListener {
        AlertDialog.Builder(activity)
            .setTitle("Confirmation")
            .setMessage("Anda yakin ingin log out?")
            .setPositiveButton("Log out") {p0, p1 ->
                requireActivity()
                    .getSharedPreferences(Global.PREFS_NAME, Context.MODE_PRIVATE)
                    .edit()
                    .remove("token")
                    .apply()
                requireActivity()
                    .startActivity(Intent(activity, AuthActivity::class.java))

                requireActivity().finish()
            }
            .setNegativeButton("Cancel", null)
            .show()
        }
    }
}