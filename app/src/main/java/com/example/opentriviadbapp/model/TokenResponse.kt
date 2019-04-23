package com.example.opentriviadbapp.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class TokenResponse(
    @JsonProperty("response_code") var responseCode: Int,
    @JsonProperty("response_message") var responseMessage: String,
    @JsonProperty("token") var token: String
)