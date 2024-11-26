package com.bsi.entrymonitoring.model

data class Employee(
    val cardNo : String,
    val idBadge : String,
    val name : String,
    val department : String,
    val company : String,
    val status : Int,
    val photo : String
)
