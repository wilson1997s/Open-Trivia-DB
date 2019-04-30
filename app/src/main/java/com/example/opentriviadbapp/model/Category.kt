package com.example.opentriviadbapp.model

import com.example.opentriviadbapp.Constant
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Category(
    @JsonProperty(Constant.JSON_ID) val id: Int,
    @JsonProperty(Constant.JSON_NAME) var name: String = ""
) {
    var totalQuestion: Int = 0
    var totalEasy: Int = 0
    var totalMedium: Int = 0
    var totalHard: Int = 0
}