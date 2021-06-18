package io.dotanuki.magicmodules.fixtures.utils

object GenerateRandomName {

    operator fun invoke(): String = options.random()

    private val options = listOf(
        "Al Pacino",
        "Robert de Niro",
        "Marlon Brando",
        "Daniel Day-Lewis",
        "Clint Eastwood",
        "Harrison Ford",
        "Sean Conery"
    )
}