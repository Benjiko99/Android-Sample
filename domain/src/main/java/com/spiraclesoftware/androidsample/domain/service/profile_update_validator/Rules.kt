package com.spiraclesoftware.androidsample.domain.service.profile_update_validator

object FieldIsNotBlankRule : Rule() {

    override fun isSatisfiedBy(input: String) =
        input.isNotBlank()

}

abstract class Rule {

    abstract fun isSatisfiedBy(input: String): Boolean

    fun isDissatisfiedBy(input: String): Boolean =
        isSatisfiedBy(input).not()

}
