package com.example.opentriviadbapp.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


@JsonIgnoreProperties(ignoreUnknown = true)
data class CategoryCount(
    @JsonProperty("total_question_count") var totalQuestion: Int = 0,
    @JsonProperty("total_easy_question_count") var totalEasy: Int = 0,
    @JsonProperty("total_medium_question_count") var totalMedium: Int = 0,
    @JsonProperty("total_hard_question_count") var totalHard: Int = 0
) {

}