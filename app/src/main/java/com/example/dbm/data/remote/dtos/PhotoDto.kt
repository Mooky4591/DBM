package com.example.dbm.data.remote.dtos

import com.example.dbm.job.constants.QuestionIds

data class PhotoDto (
    val photo: ByteArray,
    val questionIds: QuestionIds
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PhotoDto

        if (!photo.contentEquals(other.photo)) return false
        if (questionIds != other.questionIds) return false

        return true
    }

    override fun hashCode(): Int {
        var result = photo.contentHashCode()
        result = 31 * result + questionIds.hashCode()
        return result
    }
}