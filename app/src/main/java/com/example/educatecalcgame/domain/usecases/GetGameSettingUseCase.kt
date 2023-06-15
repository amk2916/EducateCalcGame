package com.example.educatecalcgame.domain.usecases

import com.example.educatecalcgame.domain.entity.GameSetting
import com.example.educatecalcgame.domain.entity.Level
import com.example.educatecalcgame.domain.repositories.GameRepository

/**
 * генерация параметров игры в зависимости от выбранного уровня
 */
class GetGameSettingUseCase(private val repository: GameRepository) {
    operator fun invoke(level:Level) : GameSetting{
        return repository.getGameSetting(level)
    }
}