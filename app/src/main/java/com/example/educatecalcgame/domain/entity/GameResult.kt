package com.example.educatecalcgame.domain.entity

/**
 * GameResult - результаты игры
 * winner - победил/проишрал игрок
 * countOfRightAnswers - количество данных правильных ответов за игру
 * countOfQuestion - общее количестов вопросов на которые игрок дал ответ
 * gameSetting - игровые настройки - для сравнения результатов
 */
data class GameResult(
    val winner: Boolean,
    val countOfRightAnswers: Int,
    val countOfQuestion: Int,
    val gameSetting: GameSetting
)
