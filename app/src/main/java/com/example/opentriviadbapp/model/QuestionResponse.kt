package com.example.opentriviadbapp.model

import com.example.opentriviadbapp.Constant
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

//for Api's Json response
@JsonIgnoreProperties(ignoreUnknown = true)
data class QuestionResponse(
    @JsonProperty(Constant.JSON_RESPONSE_CODE) var responseCode: Int,
    @JsonProperty(Constant.JSON_RESPONSE_RESULT) var results: List<Question>
)