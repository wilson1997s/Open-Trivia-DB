package com.example.opentriviadbapp.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


@JsonIgnoreProperties(ignoreUnknown = true)
data class Category(
    @JsonProperty("id") val id: Int
) {
    @JsonProperty("name")
    var name: String = ""
    var totalQuestion: Int = 0
    var totalEasy: Int = 0
    var totalMedium: Int = 0
    var totalHard: Int = 0

}