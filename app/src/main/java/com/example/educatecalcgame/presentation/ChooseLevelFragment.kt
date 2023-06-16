package com.example.educatecalcgame.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.educatecalcgame.R
import com.example.educatecalcgame.databinding.ChooseFragmentLevelBinding
import com.example.educatecalcgame.databinding.FragmentWelcomeBinding

class ChooseLevelFragment : Fragment() {
    private var _binding: ChooseFragmentLevelBinding? = null
    private val binding: ChooseFragmentLevelBinding
        get() = _binding ?: throw RuntimeException("ChooseLevelFragment is null")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ChooseFragmentLevelBinding.inflate(inflater, container,false)
        return binding.root
    }
}