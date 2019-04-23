package com.example.opentriviadbapp.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty


@JsonIgnoreProperties(ignoreUnknown = true)
data class QuestionResponse(
    @JsonProperty("response_code") var responseCode: Int,
    @JsonProperty("results") var results: List<Question>
) {

}