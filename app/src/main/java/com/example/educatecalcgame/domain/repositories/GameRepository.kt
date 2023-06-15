package com.example.educatecalcgame.domain.repositories

import com.example.educatecalcgame.domain.entity.GameSetting
import com.example.educatecalcgame.domain.entity.Level
import com.example.educatecalcgame.domain.entity.Question

interface GameRepository {

    fun generateQuestion(
        maxSumValue:Int,
        countOfOption:Int
    ): Question

    fun getGameSetting(level:Level):GameSetting
}