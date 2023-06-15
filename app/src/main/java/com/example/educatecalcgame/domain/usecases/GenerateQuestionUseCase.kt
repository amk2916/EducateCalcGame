package com.example.educatecalcgame.domain.usecases

import com.example.educatecalcgame.domain.entity.Question
import com.example.educatecalcgame.domain.repositories.GameRepository

/**
 * получаем вопрос во время игры
 */
class GenerateQuestionUseCase(private val repository: GameRepository) {

    //переопределили инвок, чтобы с объектом USeCase работать как с методом
    operator fun invoke(maxSumValue: Int): Question{
        return repository.generateQuestion(maxSumValue, COUNT_OF_OPTIONS)
    }

    private companion object{
        //какое количество вариантов ответов генерировать
        private const val COUNT_OF_OPTIONS = 6
    }

}