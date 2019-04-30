package com.example.opentriviadbapp.model

import com.example.opentriviadbapp.Constant
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

//for Api's Json response
@JsonIgnoreProperties(ignoreUnknown = true)
data class CategoryListResponse(
    @JsonProperty(Constant.JSON_TRIVIA_CATEGORY) val categoryList: List<Category>
)