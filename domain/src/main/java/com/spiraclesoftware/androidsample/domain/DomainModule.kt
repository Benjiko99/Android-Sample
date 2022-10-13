package com.spiraclesoftware.androidsample.domain

import com.spiraclesoftware.androidsample.domain.interactor.interactorModule
import com.spiraclesoftware.androidsample.domain.service.serviceModule

val domainModules = interactorModule + serviceModule
