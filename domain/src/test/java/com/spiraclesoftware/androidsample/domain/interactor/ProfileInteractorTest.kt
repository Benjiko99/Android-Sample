package com.spiraclesoftware.androidsample.domain.interactor

import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.LocalDataSource
import com.spiraclesoftware.androidsample.domain.RemoteDataSource
import com.spiraclesoftware.androidsample.domain.entity.Profile
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileInteractorTest {

    @MockK
    lateinit var localDataSource: LocalDataSource

    @MockK
    lateinit var remoteDataSource: RemoteDataSource

    @InjectMockKs
    lateinit var testSubject: ProfileInteractor

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
    }

    @Test
    fun getProfile_fromCache() = runBlockingTest {
        val mockProfile = mockk<Profile>()

        every { localDataSource.getProfile() } returns mockProfile

        assertThat(testSubject.getProfile()).isEqualTo(mockProfile)
    }

}
