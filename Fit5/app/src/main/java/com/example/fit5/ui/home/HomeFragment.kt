package com.example.fit5.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.fit5.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
//variables
    private var _binding: FragmentHomeBinding? = null
    lateinit var cardioBtn: Button
    lateinit var chestBtn: Button
    lateinit var backBtn: Button

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //type casting using binding
        cardioBtn = binding.cardioBtn
        chestBtn = binding.ChestBtn
        backBtn = binding.BackBtn

        //onclick for cardio button
        cardioBtn.setOnClickListener {
            Toast.makeText(requireContext(), "You are trying to start a cardio workout", Toast.LENGTH_SHORT).show()
        }
        //onclick for chest button
        chestBtn.setOnClickListener {
            Toast.makeText(requireContext(), "you are trying to start a chest workout", Toast.LENGTH_SHORT).show()
        }

        //onclick for back button
        backBtn.setOnClickListener {
            Toast.makeText(requireContext(), "you are trying to do a back workout", Toast.LENGTH_SHORT).show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }





}