package com.ubaya.protectcare23.data

//Class Lokasi
data class Location(val code: String, val name: String) {
    override fun toString(): String {
        return name
    }
}
