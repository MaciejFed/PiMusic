package com.fedorowiat.spotify

import com.wrapper.spotify.SpotifyApi
import java.lang.Thread.sleep
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.FutureTask
import java.util.concurrent.TimeUnit

private const val userId = "1191687518"
private const val raspberryId = "98bb0735e28656bac098d927d410c3138a4b5bca"
private var cancelExecutor = Executors.newSingleThreadExecutor()

fun SpotifyApi.startResumeUsersPlayback(playlistContext: String) {
    startResumeUsersPlayback()
            .device_id(raspberryId)
            .context_uri(playlistContext)
            .build()
            .execute()
    cancelExecutor.shutdownNow()
    cancelExecutor = Executors.newSingleThreadExecutor()
    cancelExecutor.execute(FutureTask(Callable { {
        sleep(TimeUnit.MINUTES.toMillis(45))
        pause()
    } }.call()))

}

fun SpotifyApi.pause() {
    pauseUsersPlayback()
            .device_id(raspberryId)
            .build()
            .execute()
}

fun SpotifyApi.toggleShuffle(on: Boolean) {
    toggleShuffleForUsersPlayback(on)
            .device_id(raspberryId)
            .build()
            .execute()
    next()
}

fun SpotifyApi.next() {
    skipUsersPlaybackToNextTrack()
            .device_id(raspberryId)
            .build()
            .execute()
}