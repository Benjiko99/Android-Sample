package com.spiraclesoftware.androidsample.domain.service.profile_update_validator

import com.spiraclesoftware.androidsample.domain.entity.Profile
import com.spiraclesoftware.androidsample.domain.interactor.ProfileInteractor.ProfileUpdateData
import com.spiraclesoftware.androidsample.domain.service.profile_update_validator.ProfileUpdateValidator.Error
import com.spiraclesoftware.androidsample.domain.service.profile_update_validator.ProfileUpdateValidator.ValidationResult
import com.spiraclesoftware.androidsample.domain.service.profile_update_validator.ProfileUpdateValidator.ValidationResult.Invalid
import com.spiraclesoftware.androidsample.domain.service.profile_update_validator.ProfileUpdateValidator.ValidationResult.Valid

/**
 * Validates login credentials.
 *
 * To use it, call [sanitizeAndValidate] with your [ProfileUpdateData] and check the returned [ValidationResult].
 *
 * To add new validations, define the rules which need to be satisfied
 * and the errors that can be thrown in response to dissatisfied rules.
 *
 * For each field in your login form define a mapping of [Rule]s to [Error]s
 * which apply to the given field.
 */
class ProfileUpdateValidator {

    abstract class Error

    interface NameField
    interface DateOfBirthField
    interface PhoneNumberField
    interface EmailField

    // Declare which rules apply to which field and what error to return
    // if the rule is not satisfied.
    private val nameFieldRules = mapOf<Rule, Error>(
        FieldIsNotBlankRule to NameIsBlank
    )

    private val dateOfBirthFieldRules = mapOf<Rule, Error>(
        FieldIsNotBlankRule to DateOfBirthIsBlank
    )

    private val phoneNumberFieldRules = mapOf<Rule, Error>(
        FieldIsNotBlankRule to PhoneNumberIsBlank
    )

    private val emailFieldRules = mapOf<Rule, Error>(
        FieldIsNotBlankRule to EmailIsBlank
    )

    fun sanitizeAndValidate(profileUpdateData: ProfileUpdateData): ValidationResult {
        val sanitizedProfileUpdate = with(profileUpdateData) {
            profileUpdateData.copy(
                fullName = fullName.trim(),
                dateOfBirth = dateOfBirth.trim(),
                phoneNumber = phoneNumber.trim(),
                email = email.trim(),
            )
        }

        return validate(sanitizedProfileUpdate)
    }

    private fun validate(profileUpdateData: ProfileUpdateData): ValidationResult {
        val errors = mutableListOf<Error>()

        with(profileUpdateData) {
            validateInputAgainstRules(fullName, nameFieldRules).also(errors::addAll)
            validateInputAgainstRules(dateOfBirth, dateOfBirthFieldRules).also(errors::addAll)
            validateInputAgainstRules(phoneNumber, phoneNumberFieldRules).also(errors::addAll)
            validateInputAgainstRules(email, emailFieldRules).also(errors::addAll)
        }

        return if (errors.isEmpty()) Valid(profileUpdateData.toProfile()) else Invalid(
            errors
        )
    }

    private fun validateInputAgainstRules(input: String, rules: Map<Rule, Error>): List<Error> {
        val errors = mutableListOf<Error>()

        rules.forEach { (rule, error) ->
            if (rule.isDissatisfiedBy(input))
                errors.add(error)
        }
        return errors
    }

    sealed class ValidationResult {

        data class Valid(
            val sanitizedProfile: Profile
        ) : ValidationResult()

        data class Invalid(
            val errors: List<Error>
        ) : ValidationResult()

    }

}
