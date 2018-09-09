package com.fedorowiat.spotify

import com.wrapper.spotify.SpotifyApi

private const val userId = "1191687518"
private const val raspberryId = "98bb0735e28656bac098d927d410c3138a4b5bca"

fun SpotifyApi.startResumeUsersPlayback(playlistContext: String) {
    startResumeUsersPlayback()
            .device_id(raspberryId)
            .context_uri(playlistContext)
            .build()
            .execute()
}