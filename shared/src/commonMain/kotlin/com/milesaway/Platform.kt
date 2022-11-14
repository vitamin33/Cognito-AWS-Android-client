package com.milesaway

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform