package com.spiraclesoftware.androidsample.domain.interactor

import com.spiraclesoftware.androidsample.domain.LocalDataSource
import com.spiraclesoftware.androidsample.domain.RemoteDataSource

class ProfileInteractor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {

}
