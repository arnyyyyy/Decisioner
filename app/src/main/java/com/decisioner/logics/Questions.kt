package com.decisioner.logics


data class Question(val text: String, val coefficients: Coefficients)
data class Coefficients(val yes: Float, val mostlyYes: Float, val mostlyNo: Float, val no: Float)


