package com.example.educatecalcgame.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.educatecalcgame.R
import com.example.educatecalcgame.databinding.FragmentGameBinding
import com.example.educatecalcgame.domain.entity.GameResult
import com.example.educatecalcgame.domain.entity.GameSetting
import com.example.educatecalcgame.domain.entity.Level

class GameFragment : Fragment() {

    private lateinit var level: Level
    /*
        к вью надо обращаться между onCreateView и onDestroyView
        не включительно, для этого и для того что бы не проверять постоянно на
        null создаем две переменные , одну нулабельную, которой
        передаем макет, у второй переопределяем геттер и используем через нее
        если в ней не будет вью, мы это увидим. В методе onDestroyView надо занулить переменну
     */
    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("GameFinishedFragment is null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvOption1.setOnClickListener{
            launchGameFinishedFragment(
                GameResult(
                winner = true,
                countOfQuestion = 10,
                countOfRightAnswers = 10,
                gameSetting = GameSetting(
                    10,
                    10,
                    100,
                    10
                )
            )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchGameFinishedFragment(gameResult: GameResult){
        requireActivity().supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_container,GameFinishedFragment.newInstance(gameResult))
            .addToBackStack(null)
            .commit()
    }

    private fun parseArgs(){
        requireArguments().getParcelable<Level>(KEY_LEVEL)?.let {
            level = it
        }
    }

    companion object {
        const val GAME_NAME_BACKSTACK = "gameFragment"
        private const val KEY_LEVEL = "level"
        fun newInstance(level: Level): GameFragment {
            return GameFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_LEVEL, level)
                }
            }
        }
    }
}