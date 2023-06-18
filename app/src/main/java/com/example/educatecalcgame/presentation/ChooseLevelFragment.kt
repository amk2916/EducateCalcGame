package com.example.educatecalcgame.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.educatecalcgame.R
import com.example.educatecalcgame.databinding.ChooseFragmentLevelBinding
import com.example.educatecalcgame.databinding.FragmentWelcomeBinding
import com.example.educatecalcgame.domain.entity.Level

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            buttonLevelTest.setOnClickListener{
                launchGameFragment(Level.TEST)
            }
            buttonLevelEasy.setOnClickListener{
                launchGameFragment(Level.EASY)
            }
            buttonLevelNormal.setOnClickListener{
                launchGameFragment(Level.NORMAL)
            }
            buttonLevelHard.setOnClickListener{
                launchGameFragment(Level.HARD)
            }

        }

    }

    private fun launchGameFragment(level:Level){
        findNavController().navigate(
            ChooseLevelFragmentDirections.actionChooseLevelFragmentToGameFragment(level)
        )
    }

    companion object{

        const val CHOOSE_LEVEL_NAME_BACKSTACK = "chooseLevelFragment"
        fun newInstance():ChooseLevelFragment{
            return ChooseLevelFragment()
        }
    }
}