package com.spiraclesoftware.androidsample.feature.profile

import com.spiraclesoftware.androidsample.domain.interactor.ProfileInteractor
import com.spiraclesoftware.androidsample.format.ExceptionFormatter
import com.spiraclesoftware.androidsample.framework.StandardPresenter

class ProfilePresenter(
    private val profileInteractor: ProfileInteractor,
    private val profileFormatter: ProfileFormatter,
    exceptionFormatter: ExceptionFormatter
) : StandardPresenter(exceptionFormatter) {

    fun getProfileModel(): ProfileModel {
        return profileInteractor.getProfile().let(profileFormatter::profileModel)
    }

}