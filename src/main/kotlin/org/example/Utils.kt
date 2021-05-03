package org.example

fun requiredProperty(propertyName: String) =
    requireNotNull(System.getProperty(propertyName), { "$propertyName property is mandatory" })
