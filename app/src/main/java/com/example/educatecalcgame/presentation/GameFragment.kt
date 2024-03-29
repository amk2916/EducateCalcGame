package com.example.educatecalcgame.presentation

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.educatecalcgame.R
import com.example.educatecalcgame.databinding.FragmentGameBinding
import com.example.educatecalcgame.domain.entity.GameResult
import com.example.educatecalcgame.domain.entity.Level

class GameFragment : Fragment() {
    //получаем аргументы по навигации
    private val args by navArgs<GameFragmentArgs>()

    private val gameViewModelFactory by lazy {
        //второй метод получения аргументов
       // val args = GameFragmentArgs.fromBundle(requireArguments())
        GameViewModelFactory(args.level, requireActivity().application)
    }
    //подписываемся на вью модель
    private val viewModel by lazy {
        ViewModelProvider(this, gameViewModelFactory)[GameViewModel::class.java]
    }
    //Линивая инициализация для вариантов ответов
    private val tvOptions by lazy {
        mutableListOf<TextView>().apply {
            add(binding.tvOption1)
            add(binding.tvOption2)
            add(binding.tvOption3)
            add(binding.tvOption4)
            add(binding.tvOption5)
            add(binding.tvOption6)
        }
    }

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
        observeViewModel()
        setClickListenersToOptions()
    }
    //установка слушателей на варианты ответов
    private fun setClickListenersToOptions(){
        for(tvOption in tvOptions){
            tvOption.setOnClickListener {
                viewModel.chooseAnswer(tvOption.text.toString().toInt())
            }
        }
    }
    //подписываемся на все свойства вью модели для обновления значений во вью
    private fun observeViewModel(){
        with(viewModel) {
            question.observe(viewLifecycleOwner) {
                binding.tvSum.text = it.sum.toString()
                binding.tvLeftNumber.text = it.visibleNumber.toString()
                for (i in 0 until tvOptions.size) {
                    tvOptions[i].text = it.options[i].toString()
                }
            }
            percentRightAnswers.observe(viewLifecycleOwner) {
                binding.progressBar.setProgress(it, true)
            }
            //изменение цвета текста в зависимости от количества введенных правильных ответов
            enoughCount.observe(viewLifecycleOwner) {
                binding.tvAnswersProgress.setTextColor(getColorByState(it))
            }
            //установка цвета прогресс бара
            enoughPercent.observe(viewLifecycleOwner) {
                binding.progressBar.progressTintList = ColorStateList.valueOf(getColorByState(it))
            }
            formattedTime.observe(viewLifecycleOwner) {
                binding.tvTimer.text = it
            }
            minPercent.observe(viewLifecycleOwner) {
                binding.progressBar.secondaryProgress = it
            }
            gameResult.observe(viewLifecycleOwner) {
                launchGameFinishedFragment(it)
            }
            progressRightAnswers.observe(viewLifecycleOwner) {
                binding.tvAnswersProgress.text = it
            }
        }
    }
    //получение цвета в зависимости от состаяния
    private fun getColorByState(goodState: Boolean) : Int{
        val colorResId =  if (goodState) {
            android.R.color.holo_green_light
        } else {
            android.R.color.holo_red_light
        }
        return ContextCompat.getColor(requireContext(),colorResId)
    }
    //уничтожение вью связанных с фрагментом, зануляем ссылки в экземпляре binding
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    // метод открытия следующего фрагмента
    private fun launchGameFinishedFragment(gameResult: GameResult) {
        findNavController().navigate(
            GameFragmentDirections.actionGameFragmentToGameFinishedFragment(gameResult)
        )
    }

}