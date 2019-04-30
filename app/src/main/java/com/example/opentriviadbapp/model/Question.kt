package com.example.opentriviadbapp.model

import com.example.opentriviadbapp.Constant
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


@JsonIgnoreProperties(ignoreUnknown = true)
data class Question(
    @JsonProperty(Constant.JSON_CATEGORY ) var category: String,
    @JsonProperty(Constant.JSON_TYPE) var type: String,
    @JsonProperty(Constant.JSON_DIFFICULTY) var difficulty: String,
    @JsonProperty(Constant.JSON_QUESTION) var question: String,
    @JsonProperty(Constant.JSON_CORRECT_ANS) var correctAnswer: String,
    @JsonProperty(Constant.JSON_INCORRECT_ANS) var incorrectAnswer: List<String>
)