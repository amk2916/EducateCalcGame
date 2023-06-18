package com.example.educatecalcgame.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.educatecalcgame.R
import com.example.educatecalcgame.databinding.FragmentGameFinishedBinding
import com.example.educatecalcgame.domain.entity.GameResult

class GameFinishedFragment : Fragment() {
    private var _binding: FragmentGameFinishedBinding? = null
    private lateinit var gameResult: GameResult
    private val binding: FragmentGameFinishedBinding
        get() = _binding ?: throw RuntimeException("GameFinishedFragment is null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArg()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOnClickListener()
        bindViews()


    }

    private fun setupOnClickListener() {
        binding.buttonRetry.setOnClickListener {
            retryGame()
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(
                true
            ) {
                override fun handleOnBackPressed() {
                    retryGame()
                }

            }
        )
    }

    private fun bindViews(){
        with(binding){
            emojiResult.setImageResource(getSmileResId())

            tvRequiredAnswers.text = String.format(
                getString(R.string.required_score),
                gameResult.gameSetting.minCountOfRightAnswers
            )

            tvScoreAnswers.text = String.format(
                getString(R.string.score_answers),
                gameResult.countOfRightAnswers
            )

            tvRequiredPercentage.text = String.format(
                getString(R.string.required_percentage),
                gameResult.gameSetting.minPercentOfRightAnswers
            )

            tvScorePercentage.text = String.format(
                getString(R.string.score_percentage),
                getPercentOfRightAnswers()
            )

        }
    }

    private fun getPercentOfRightAnswers() = with(gameResult){
        if(countOfQuestion ==0){
            0
        }else{
            ((countOfRightAnswers / countOfQuestion.toDouble())*100).toInt()
        }
    }
    private fun getSmileResId():Int{
        return if(gameResult.winner){
            R.drawable.ic_smile
        }else{
            R.drawable.ic_sad
        }
    }

    private fun retryGame() {
        requireActivity().supportFragmentManager
            //FragmentManager.POP_BACK_STACK_INCLUSIVE - удалит из бэкстека все фрагменты до указанного, включая его
            // A -> B -> C -> D передаем B и этот флаг, удалится все до А
            .popBackStack(
                GameFragment.GAME_NAME_BACKSTACK,
                FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
    }

    private fun parseArg() {
        requireArguments().getParcelable<GameResult>(KEY_GAME_RESULT)?.let {
            gameResult = it
        }
    }

    companion object {
        private const val KEY_GAME_RESULT = "gameResult"

        fun newInstance(gameResult: GameResult): GameFinishedFragment {
            return GameFinishedFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_GAME_RESULT, gameResult)
                }
            }
        }
    }
}