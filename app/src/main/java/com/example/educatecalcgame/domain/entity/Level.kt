package com.example.educatecalcgame.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

/**
 * Уровень сложности игры
 * enum классы неявно реализуют сриалайзбл, поэтому явно указывать
 * не нужно
 */
@Parcelize
enum class Level :Parcelable {
    TEST, EASY, NORMAL, HARD
}