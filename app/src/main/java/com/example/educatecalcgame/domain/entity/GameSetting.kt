package com.example.educatecalcgame.domain.entity

/**
 * GameSetting - настройки игры
 * maxSumValue - максимально возможная сумма
 * minCountOfRightAnswers - минимальное количество правильных ответов для победы
 * minPercentOfRightAnswers - минимальный процент правильных ответов для победы
 * gameTimeInSecond - игровое время в секундах
 */
data class GameSetting(
    val maxSumValue: Int,
    val minCountOfRightAnswers: Int,
    val minPercentOfRightAnswers: Int,
    val gameTimeInSecond: Int
)
