package com.example.educatecalcgame.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.educatecalcgame.R
import com.example.educatecalcgame.data.GameRepositoryImpl
import com.example.educatecalcgame.domain.entity.GameResult
import com.example.educatecalcgame.domain.entity.GameSetting
import com.example.educatecalcgame.domain.entity.Level
import com.example.educatecalcgame.domain.entity.Question
import com.example.educatecalcgame.domain.usecases.GenerateQuestionUseCase
import com.example.educatecalcgame.domain.usecases.GetGameSettingUseCase

//AndroidViewModel
class GameViewModel(application: Application) : AndroidViewModel(application) {

    private lateinit var gameSetting: GameSetting
    private lateinit var level: Level
    private val context = application

    private val repository = GameRepositoryImpl

    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingUseCase = GetGameSettingUseCase(repository)

    private var timer: CountDownTimer? = null

    private var countOfRightAnswers = 0
    private var countOfQuestion = 0

    private val _percentRightAnswers = MutableLiveData<Int>()
    val percentRightAnswers: LiveData<Int>
        get() = _percentRightAnswers


    private val _progressRightAnswers = MutableLiveData<String>()
    val progressRightAnswers: LiveData<String>
        get() = _progressRightAnswers

    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val _enoughCount = MutableLiveData<Boolean>()
    val enoughCount: LiveData<Boolean>
        get() = _enoughCount

    private val _enoughPercent = MutableLiveData<Boolean>()
    val enoughPercent: LiveData<Boolean>
        get() = _enoughPercent

    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    fun startGame(level: Level) {
        getGameSettings(level)
        startTimer()
        generateQuestion()
    }

    fun chooseAnswer(number: Int) {
        checkAnswer(number)
        updateProgress()
        generateQuestion()
    }

    private fun updateProgress() {
        val percent = calculatePercentOfRightAnswers()
        _percentRightAnswers.value = percent
        _progressRightAnswers.value = String.format(
            context.resources.getString(R.string.progress_answers),
            countOfRightAnswers,
            gameSetting.minCountOfRightAnswers
        )
        _enoughCount.value = countOfRightAnswers >= gameSetting.minCountOfRightAnswers
        _enoughPercent.value = percent >= gameSetting.minPercentOfRightAnswers
    }

    private fun calculatePercentOfRightAnswers(): Int =
        ((countOfRightAnswers / countOfQuestion.toDouble()) * 100).toInt()

    private fun checkAnswer(number: Int) {
        val rightAnswer = question.value?.rightAnswer
        if (number == rightAnswer) {
            countOfRightAnswers++
        }
        countOfQuestion++
    }

    private fun getGameSettings(level: Level) {
        this.level = level
        this.gameSetting = getGameSettingUseCase(level)
        _minPercent.value = gameSetting.minPercentOfRightAnswers
    }

    private fun startTimer() {
        timer = object : CountDownTimer(
            gameSetting.gameTimeInSecond * MILLIS_IN_SECONDS,
            MILLIS_IN_SECONDS
        ) {
            override fun onTick(millisUntilFinished: Long) {
                _formattedTime.value = formatTime(millisUntilFinished)
            }

            override fun onFinish() {
                finishGame()
            }
        }
        timer?.start()
    }

    private fun formatTime(millisUntilFinished: Long): String {
        val seconds = millisUntilFinished / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTES)
        return String.format("%02d:%02d", minutes, seconds)
    }

    private fun finishGame() {
        _gameResult.value = GameResult(
            enoughCount.value == true && enoughPercent.value == true,
            countOfRightAnswers,
            countOfQuestion,
            gameSetting
        )
    }

    private fun generateQuestion() {
        _question.value = generateQuestionUseCase(gameSetting.maxSumValue)
    }

    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }

    companion object {
        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECONDS_IN_MINUTES = 60
    }


}