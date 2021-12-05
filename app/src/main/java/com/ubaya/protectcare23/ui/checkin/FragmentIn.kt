package com.ubaya.protectcare23.ui.checkin

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import com.ubaya.protectcare23.api.ApiService
import com.ubaya.protectcare23.databinding.FragmentInBinding
import com.ubaya.protectcare23.di.Injection
import com.ubaya.protectcare23.utils.Global
import kotlinx.coroutines.launch
import com.ubaya.protectcare23.listener.OnCheckListener


class FragmentIn(listener: OnCheckListener) : Fragment() {


    private lateinit var binding: FragmentInBinding
    private lateinit var apiService: ApiService
    private val mListener = listener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentInBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiService = Injection.provideService(requireContext())

        val token = requireActivity().getSharedPreferences(Global.PREFS_NAME, Context.MODE_PRIVATE)
            .getString(Global.PREFS_NAME, "").toString()

        viewLifecycleOwner.lifecycleScope.launch {
            val locations = apiService.getLocation()

            val adapterLocation = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_dropdown_item,
                locations
            )

            binding.spinerLocation.apply {
                adapter = adapterLocation
                onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(
                        p0: AdapterView<*>?,
                        p1: View?,
                        position: Int,
                        p3: Long
                    ) {
                        binding.uniqueCode.editText?.setText(locations[position].code)
                    }

                    override fun onNothingSelected(p0: AdapterView<*>?) {

                    }
                }
            }
        }

        binding.btnCheckin.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val chkin = apiService.checkIn(
                    token,
                    binding.uniqueCode.editText?.text.toString()
                )

                Global.checkedIn = chkin
                mListener.OnButtonClicked()
            }
        }
    }


}

