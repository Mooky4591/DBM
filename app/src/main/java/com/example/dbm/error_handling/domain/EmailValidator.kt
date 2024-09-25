package com.example.dbm.error_handling.domain;

class EmailValidator {

    fun validateEmail(email: String): Result<Unit, EmailError> {
        val domain = email.substringAfter("@")
        val localPart = email.substringBefore("@")
        if (!email.contains("@") || email.count { it == '@'} > 1) {
            return Result.Error(EmailError.MUST_CONTAIN_ONE_AT_SYMBOL)
        }

        if (email.startsWith("@")) {
            return Result.Error(EmailError.CANNOT_START_WITH_AT_SYMBOL)
        }

        if (email.endsWith("@")) {
            return Result.Error(EmailError.CANNOT_END_WITH_AT_SYMBOL)
        }

        if (email.startsWith(".")) {
            return Result.Error(EmailError.CANNOT_START_WITH_DOT)
        }

        if (email.endsWith(".")) {
            return Result.Error(EmailError.CANNOT_END_WITH_DOT)
        }
        if (!localPart.matches(Regex("[a-zA-Z0-9._%+-]+"))) {
            return Result.Error(EmailError.INVALID_LOCAL_PART)
        }
        if (email.contains("..")) {
            return Result.Error(EmailError.CONSECUTIVE_DOTS)
        }

        if (localPart.length > 64 || domain.length > 255) {
            return Result.Error(EmailError.TOO_LONG)
        }

        val tld = domain.substringAfterLast(".")
        if (tld.length < 2) {
            return Result.Error(EmailError.INVALID_TLD)
        }
        return Result.Success(Unit)
    }

    enum class EmailError : Error {
        MUST_CONTAIN_ONE_AT_SYMBOL,
        CANNOT_START_WITH_AT_SYMBOL,
        CANNOT_END_WITH_AT_SYMBOL,
        CANNOT_START_WITH_DOT,
        CANNOT_END_WITH_DOT,
        INVALID_LOCAL_PART,
        CONSECUTIVE_DOTS,
        TOO_LONG,
        INVALID_TLD
    }
}