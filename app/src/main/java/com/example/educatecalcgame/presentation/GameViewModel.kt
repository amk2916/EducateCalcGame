package com.example.educatecalcgame.presentation

import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.educatecalcgame.R
import com.example.educatecalcgame.data.GameRepositoryImpl
import com.example.educatecalcgame.domain.entity.GameResult
import com.example.educatecalcgame.domain.entity.GameSetting
import com.example.educatecalcgame.domain.entity.Level
import com.example.educatecalcgame.domain.entity.Question
import com.example.educatecalcgame.domain.usecases.GenerateQuestionUseCase
import com.example.educatecalcgame.domain.usecases.GetGameSettingUseCase

//AndroidViewModel
class GameViewModel(
    private val level: Level,
    private val application: Application
) : ViewModel() {
    // настройки игры в зависимости от сложности
    private lateinit var gameSetting: GameSetting
    // уровень сложности
  //  private lateinit var level: Level
    //контекст, для того чтобы использовать строковые ресурсы
  //  private val context = application

    private val repository = GameRepositoryImpl
    private val generateQuestionUseCase = GenerateQuestionUseCase(repository)
    private val getGameSettingUseCase = GetGameSettingUseCase(repository)

    //переменная - таймер, запучкаем заполняем, останавливаем все во вью модели
    private var timer: CountDownTimer? = null

    //количество правильных ответов, после каждого введенного ответа
    private var countOfRightAnswers = 0

    //количество отвеченных вопросов
    private var countOfQuestion = 0

    //процент правильных ответов, будем подписываться на них во вью,
    // чтобы получать актуальные обновления
    //percentRightAnswers - просто лайвдата, чтобы нельзя было изменять во вне
    //внутри тоже не меняем , просто переопределяем геттер
    //для остального тоже самое
    private val _percentRightAnswers = MutableLiveData<Int>()
    val percentRightAnswers: LiveData<Int>
        get() = _percentRightAnswers

    //строка прогресса: сколько ответили, сколько осталось
    private val _progressRightAnswers = MutableLiveData<String>()
    val progressRightAnswers: LiveData<String>
        get() = _progressRightAnswers
    //отформатированное время в виде (ММ:СС)
    //строка которую будет показывать фрагмент в элементе таймера
    private val _formattedTime = MutableLiveData<String>()
    val formattedTime: LiveData<String>
        get() = _formattedTime

    //Новый сгенерированный вопрос
    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    //Хватает ли количества данных правильных ответов для победы
    private val _enoughCount = MutableLiveData<Boolean>()
    val enoughCount: LiveData<Boolean>
        get() = _enoughCount

    //хватает ли набранного процента правильных ответов для победы
    private val _enoughPercent = MutableLiveData<Boolean>()
    val enoughPercent: LiveData<Boolean>
        get() = _enoughPercent
    //Минимальный процент необходимы для победы, значение пойдет в секондориПрогресс
    private val _minPercent = MutableLiveData<Int>()
    val minPercent: LiveData<Int>
        get() = _minPercent
    //Переменная с результатами
    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult
    //Действия при старте игры
    init {
        startGame()
    }
    private fun startGame() {
        getGameSettings()
        updateProgress()
        startTimer()
        generateQuestion()
    }
    //Обработка введенного пользователем ответа
    fun chooseAnswer(number: Int) {
        checkAnswer(number)
        updateProgress()
        generateQuestion()
    }

    //Обновление значения прогресса, после введенного ответа
    private fun updateProgress() {
        val percent = calculatePercentOfRightAnswers()
        _percentRightAnswers.value = percent
        _progressRightAnswers.value = String.format(
            application.resources.getString(R.string.progress_answers),
            countOfRightAnswers,
            gameSetting.minCountOfRightAnswers
        )
        _enoughCount.value = countOfRightAnswers >= gameSetting.minCountOfRightAnswers
        _enoughPercent.value = percent >= gameSetting.minPercentOfRightAnswers
    }
    //Расчет процента полученны правильных ответов
    private fun calculatePercentOfRightAnswers(): Int {
        if (countOfQuestion==0) return 0
        return ((countOfRightAnswers / countOfQuestion.toDouble()) * 100).toInt()
    }
    //проверка введенного ответа, если верный увеличиваем счетчик полученных верных ответов
    //счетчие количества верных ответов обновляем постоянно
    private fun checkAnswer(number: Int) {
        val rightAnswer = question.value?.rightAnswer
        if (number == rightAnswer) {
            countOfRightAnswers++
        }
        countOfQuestion++
    }
    //Получаем настройки игры
    private fun getGameSettings() {
        this.gameSetting = getGameSettingUseCase(level)
        _minPercent.value = gameSetting.minPercentOfRightAnswers
    }
    //Логика таймера
    private fun startTimer() {
        //Объект принимает в миллисекундах время, у нас в настройках задано в секундах
        //поэтому умножаем на 1000, для получения значения миллисекунд типа Long
        //щелкаем таймер каждую секунду - второй параметр конструктора
        timer = object : CountDownTimer(
            gameSetting.gameTimeInSecond * MILLIS_IN_SECONDS,
            MILLIS_IN_SECONDS
        ) {
            // я так понимаю , это каждый щелчек
            override fun onTick(millisUntilFinished: Long) {
                _formattedTime.value = formatTime(millisUntilFinished)
            }
            //что делать по окончанию времени
            override fun onFinish() {
                finishGame()
            }
        }
        timer?.start()
    }
    //приведение времени таймера к нужному формату ММ:СС
    private fun formatTime(millisUntilFinished: Long): String {
        val seconds = millisUntilFinished / MILLIS_IN_SECONDS
        val minutes = seconds / SECONDS_IN_MINUTES
        val leftSeconds = seconds - (minutes * SECONDS_IN_MINUTES)
        return String.format("%02d:%02d", minutes, seconds)
    }
    //Окончание игры
    private fun finishGame() {
        _gameResult.value = GameResult(
            enoughCount.value == true && enoughPercent.value == true,
            countOfRightAnswers,
            countOfQuestion,
            gameSetting
        )
    }
    //Генерируем вопрос после каждого ответа
    private fun generateQuestion() {
        _question.value = generateQuestionUseCase(gameSetting.maxSumValue)
    }
    //После ухода с фрагмента, таймер должен отменятся
    override fun onCleared() {
        super.onCleared()
        timer?.cancel()
    }
    //константы для перевода значений
    companion object {
        private const val MILLIS_IN_SECONDS = 1000L
        private const val SECONDS_IN_MINUTES = 60
    }


}