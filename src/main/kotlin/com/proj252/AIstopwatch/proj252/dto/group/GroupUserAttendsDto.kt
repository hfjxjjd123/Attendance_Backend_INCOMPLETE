package com.proj252.AIstopwatch.proj252.dto.group

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GroupUserAttendsDto(
    @SerialName("user") val username: String,
    @SerialName("recentAttends") val recentAttends: Int
)