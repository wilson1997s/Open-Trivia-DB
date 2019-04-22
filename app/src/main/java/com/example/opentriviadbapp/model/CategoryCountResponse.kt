package com.example.opentriviadbapp.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class CategoryCountResponse(
    @JsonProperty("category_id") val id: Int,
    @JsonProperty("category_question_count") val categoryCount: CategoryCount
)