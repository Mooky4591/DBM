package com.example.dbm.account_settings.presentation

interface AccountSettingEvent {
    data object OnBackPress: AccountSettingEvent
    data class OnEmailChangeClicked(val email: String) : AccountSettingEvent
    data class OnPhoneNumberChangeClicked(val number: String) : AccountSettingEvent
    data class OnNameChangeClicked(val name: String) : AccountSettingEvent
    data object OnChangePasswordClicked : AccountSettingEvent
    data class OnCompanyNameChangeClicked(val companyName: String) : AccountSettingEvent
    data class OnCompanyAddressChangeClicked(val companyAddress: String) : AccountSettingEvent
}