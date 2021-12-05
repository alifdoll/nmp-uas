package com.ubaya.protectcare23.ui.history

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ubaya.protectcare23.R
import com.ubaya.protectcare23.api.ApiService
import com.ubaya.protectcare23.databinding.FragmentHistoryBinding
import com.ubaya.protectcare23.di.Injection
import kotlinx.coroutines.launch


class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiService = Injection.provideService(requireContext())
        val token = requireActivity()
            .getSharedPreferences("token", Context.MODE_PRIVATE)
            .getString("token", "").toString()

        viewLifecycleOwner.lifecycleScope.launch {
            val history = apiService.getCheckHistory(token)
            if(history.size == 0) {
                binding.txtNoData.visibility = View.VISIBLE
                binding.rvHistory.visibility = View.GONE
            } else {
                binding.apply {
                    rvHistory.apply {
                        layoutManager = LinearLayoutManager(requireContext())
                        adapter = HistoryAdapter(history, requireContext())
                        setHasFixedSize(true)
                    }
                }
            }

        }
    }
}