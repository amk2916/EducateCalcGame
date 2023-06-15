package com.example.educatecalcgame.data

import com.example.educatecalcgame.domain.entity.GameSetting
import com.example.educatecalcgame.domain.entity.Level
import com.example.educatecalcgame.domain.entity.Question
import com.example.educatecalcgame.domain.repositories.GameRepository
import java.lang.Integer.max
import java.lang.Math.min
import kotlin.random.Random

object GameRepositoryImpl : GameRepository {

    private const val MIN_SUM_VALUE = 2
    private const val MIN_ANSWER_VALUE = 1

    //Игровая логика формирования вопроса
    override fun generateQuestion(maxSumValue: Int, countOfOption: Int): Question {
        //то, что надо будет получить, располагается по центру экрана
        val sum  = Random.nextInt(MIN_SUM_VALUE, maxSumValue +1)
        //Видимое число, к которому надо будет прибавлять предполагаемый ответ
        val visibleNumber = Random.nextInt(MIN_ANSWER_VALUE, sum)
        //варианты ответов
        val option = HashSet<Int>()
        //добавляем к вариантам ответов - правильный
        val rightAnswer = sum - visibleNumber
        option.add(rightAnswer)
        //формируем диапазон генерации вариантов ответов
        val from = max(rightAnswer - countOfOption, MIN_ANSWER_VALUE)
        val to = min(maxSumValue, rightAnswer + countOfOption)
        //остальные варианты генерируем в заданном диапазоне
        while(option.size < countOfOption){
            option.add(Random.nextInt(from, to))
        }
        return Question(sum,visibleNumber,option.toList())
    }
    //Настройки в зависимости от уровня
    override fun getGameSetting(level: Level): GameSetting {
        return when(level) {
            Level.TEST -> GameSetting(
                maxSumValue = 10,
                minCountOfRightAnswers = 3,
                minPercentOfRightAnswers = 50,
                gameTimeInSecond = 10
            )
            Level.EASY -> GameSetting(
                maxSumValue = 10,
                minCountOfRightAnswers = 10,
                minPercentOfRightAnswers = 70,
                gameTimeInSecond = 60
            )
            Level.NORMAL -> GameSetting(
                maxSumValue = 20,
                minCountOfRightAnswers = 20,
                minPercentOfRightAnswers = 80,
                gameTimeInSecond = 45
            )
            Level.HARD -> GameSetting(
                maxSumValue = 30,
                minCountOfRightAnswers = 30,
                minPercentOfRightAnswers = 85,
                gameTimeInSecond = 45
            )
        }
    }
}