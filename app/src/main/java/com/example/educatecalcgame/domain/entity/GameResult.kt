package com.example.educatecalcgame.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

/**
 * GameResult - результаты игры
 * winner - победил/проишрал игрок
 * countOfRightAnswers - количество данных правильных ответов за игру
 * countOfQuestion - общее количестов вопросов на которые игрок дал ответ
 * gameSetting - игровые настройки - для сравнения результатов
 */

@Parcelize
data class GameResult(
    val winner: Boolean,
    val countOfRightAnswers: Int,
    val countOfQuestion: Int,
    val gameSetting: GameSetting
):Parcelable
