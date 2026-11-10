package com.hathway.projecttracker

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform