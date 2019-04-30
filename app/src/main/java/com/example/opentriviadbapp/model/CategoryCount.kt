package com.example.opentriviadbapp.model

import com.example.opentriviadbapp.Constant
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


@JsonIgnoreProperties(ignoreUnknown = true)
data class CategoryCount(
    @JsonProperty(Constant.JSON_TOTAL_COUNT) var totalQuestion: Int = 0,
    @JsonProperty(Constant.JSON_EASY_COUNT) var totalEasy: Int = 0,
    @JsonProperty(Constant.JSON_MEDIUM_COUNT) var totalMedium: Int = 0,
    @JsonProperty(Constant.JSON_HARD_COUNT) var totalHard: Int = 0
)