package org.example

fun property(propertyName: String, default: String? = null) =
    requireNotNull(System.getProperty(propertyName, default)) { "$propertyName property is mandatory" }
