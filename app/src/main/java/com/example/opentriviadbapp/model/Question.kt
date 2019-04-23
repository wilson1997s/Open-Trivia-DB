package com.example.opentriviadbapp.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


@JsonIgnoreProperties(ignoreUnknown = true)
data class Question(
    @JsonProperty("category") var category: String,
    @JsonProperty("type") var type: String,
    @JsonProperty("difficulty") var difficulty: String,
    @JsonProperty("question") var question: String,
    @JsonProperty("correct_answer") var correctAnswer: String,
    @JsonProperty("incorrect_answers") var incorrectAnswer: List<String>
) {

}