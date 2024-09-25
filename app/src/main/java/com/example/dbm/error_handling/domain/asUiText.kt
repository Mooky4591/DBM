package com.example.dbm.error_handling.domain

import com.example.dbm.R
import com.example.dbm.presentation.UiText

fun PasswordValidator.PasswordError.asUiText(): UiText {
    return when (this) {
        PasswordValidator.PasswordError.TOO_SHORT -> UiText.StringResource(
            R.string.too_short
        )

        PasswordValidator.PasswordError.NO_LOWERCASE -> UiText.StringResource(
            R.string.no_lowercase
        )

        PasswordValidator.PasswordError.NO_UPPERCASE -> UiText.StringResource(
            R.string.no_uppercase
        )

        PasswordValidator.PasswordError.NO_DIGIT -> UiText.StringResource(
            R.string.no_digit
        )
    }
}

fun EmailValidator.EmailError.asUiText(): UiText {
    return when (this) {
        EmailValidator.EmailError.MUST_CONTAIN_ONE_AT_SYMBOL -> UiText.StringResource(
            R.string.email_must_contain_one
        )
        EmailValidator.EmailError.CANNOT_START_WITH_AT_SYMBOL -> UiText.StringResource(
            R.string.EMAIL_CANNOT_START_WITH_AT_SYMBOL
        )
        EmailValidator.EmailError.CANNOT_END_WITH_AT_SYMBOL -> UiText.StringResource(
            R.string.email_cannot_end_with_at_symbol
        )
        EmailValidator.EmailError.CANNOT_START_WITH_DOT -> UiText.StringResource(
            R.string.email_cannot_start_with_dot
        )
        EmailValidator.EmailError.CANNOT_END_WITH_DOT -> UiText.StringResource(
            R.string.email_cannot_end_with_dot
        )
        EmailValidator.EmailError.INVALID_LOCAL_PART -> UiText.StringResource(
            R.string.invalid_characters
        )
        EmailValidator.EmailError.CONSECUTIVE_DOTS -> UiText.StringResource(
            R.string.email_addresses_cannot_contain_consecutive_dots
        )
        EmailValidator.EmailError.TOO_LONG -> UiText.StringResource(
            R.string.email_too_long
        )
        EmailValidator.EmailError.INVALID_TLD -> UiText.StringResource(
            R.string.invalid_tld
        )
    }
}

fun DataError.Local.asUiText(): UiText {
    return when (this) {
        DataError.Local.DISK_FULL -> UiText.StringResource(
            R.string.disk_full
        )

        DataError.Local.PERMISSION_DENIED -> UiText.StringResource(
            R.string.permission_denied
        )

        DataError.Local.FILE_NOT_FOUND -> UiText.StringResource(
            R.string.file_not_found
        )

        DataError.Local.INPUT_OUTPUT_ERROR -> UiText.StringResource(
            R.string.io_error
        )

        DataError.Local.CONNECTION_REFUSED -> UiText.StringResource(
            R.string.connection_refused
        )

        DataError.Local.UNKNOWN -> UiText.StringResource(
            R.string.unknown
        )
    }
}

fun DataError.Network.asUiText(): UiText {
    return when (this) {
        DataError.Network.UNKNOWN -> UiText.StringResource(
            R.string.unknown_network_error
        )

        DataError.Network.SERIALIZATION -> UiText.StringResource(
            R.string.serialization_error
        )

        DataError.Network.SERVER_ERROR -> UiText.StringResource(
            R.string.server_error
        )

        DataError.Network.PAYLOAD_TOO_LARGE -> UiText.StringResource(
            R.string.payload_too_large
        )

        DataError.Network.TOO_MANY_REQUESTS -> UiText.StringResource(
            R.string.too_many_requests
        )

        DataError.Network.REQUEST_TIMEOUT -> UiText.StringResource(
            R.string.request_timeout
        )

        DataError.Network.NO_INTERNET -> UiText.StringResource(
            R.string.no_internet
        )

        DataError.Network.UNKNOWN_HOST_EXCEPTION -> UiText.StringResource(
            R.string.unknown_host_exception
        )

        DataError.Network.INCORRECT_PASSWORD_OR_EMAIL -> UiText.StringResource(
            R.string.incorrect_password_or_email
        )
    }
}