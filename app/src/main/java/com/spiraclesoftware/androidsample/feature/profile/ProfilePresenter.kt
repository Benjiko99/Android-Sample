package com.spiraclesoftware.androidsample.feature.profile

import com.spiraclesoftware.androidsample.domain.interactor.ProfileInteractor
import com.spiraclesoftware.androidsample.format.ExceptionFormatter
import com.spiraclesoftware.androidsample.framework.StandardPresenter

class ProfilePresenter(
    private val profileInteractor: ProfileInteractor,
    exceptionFormatter: ExceptionFormatter
) : StandardPresenter(exceptionFormatter) {

}