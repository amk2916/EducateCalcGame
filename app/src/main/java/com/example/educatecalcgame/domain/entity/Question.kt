package com.example.educatecalcgame.domain.entity

/**
 * Question - Вопросы
 * sum - Сумма которую нужно получить
 * visibleNumber - видимая часть суммы
 * options - варианты ответов
 */
data class Question(
    val sum: Int,
    val visibleNumber: Int,
    val options: List<Int>
)
