package org.example

fun property(propertyName: String): String =
    requireNotNull(System.getProperty(propertyName)) { "$propertyName property is mandatory" }

fun optionalProperty(propertyName: String, default: String): String =
    System.getProperty(propertyName, default)

fun optionalProperty(propertyName: String): String? =
    System.getProperty(propertyName)
