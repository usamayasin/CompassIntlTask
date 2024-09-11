package com.example.compassintltask.base.mapper

interface Mapper<FROM,TO> {
    fun map(from: FROM): TO
}