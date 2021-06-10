package com.spiraclesoftware.androidsample.domain.service

import com.spiraclesoftware.androidsample.domain.service.profile_update_validator.ProfileUpdateValidator
import org.koin.dsl.module

val serviceModule = module {

    single { CurrencyConverter(get()) }

    single { ProfileUpdateValidator() }

}
