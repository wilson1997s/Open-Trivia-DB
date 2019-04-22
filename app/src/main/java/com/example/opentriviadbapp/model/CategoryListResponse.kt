package com.example.opentriviadbapp.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class CategoryListResponse(
    @JsonProperty("trivia_categories") val categoryList: List<Category>
)