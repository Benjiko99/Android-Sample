package com.spiraclesoftware.androidsample.domain.interactor

import com.google.common.truth.Truth.assertThat
import com.spiraclesoftware.androidsample.domain.LocalDataSource
import com.spiraclesoftware.androidsample.domain.RemoteDataSource
import com.spiraclesoftware.androidsample.domain.entity.Profile
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
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

    @Test
    fun updateProfile() = runBlockingTest {
        justRun { localDataSource.saveProfile(any()) }

        testSubject.updateProfile(
            fullName = "John Smith",
            dateOfBirth = "1.31.2000",
            phoneNumber = "+420 123 456 789",
            email = "john.smith@example.com"
        )

        verify { localDataSource.saveProfile(any()) }
    }

}
