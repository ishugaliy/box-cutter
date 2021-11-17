package it.devchallenge.api

fun positiveConstraint(name: String, value: Int?) {
    notNullableConstraint(name, value)
    constraint("$name must be positive") { value!! <= 0 }
}

fun <T> notNullableConstraint(name: String, value: T?) {
    constraint("$name was not set") { value == null }
}

fun constraint(message: String, violation: () -> Boolean) {
    if (violation.invoke()) throw IllegalArgumentException(message)
}
