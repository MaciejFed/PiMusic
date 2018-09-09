package com.fedorowiat.spotify

import com.fedorowiat.playlist.Playlist
import com.wrapper.spotify.SpotifyApi
import com.wrapper.spotify.SpotifyHttpManager
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

private const val clientId = "eb1b3708670741cbbb51ffdb89fe53df"
private const val clientSecret = "f85f6e6c82a64a0dad7c596d5cc581db"
private var accessToken = "BQBjn24-Bu523mgg7dwj-4eYZxre19J5cABcJpGbWSGQ-k-OW33sFJWkl3D72fDa10bQQqGuOhcf2vKZGrj8RNmSLUy5i9rIOLZWJ2ezQvOVBp77JE8PUCk6WyJh6TiU-57O45z_3hN4eSAbfUJ6e41HhJHk6SdTl7FqBOYPt30ycwiWmsYa1pjWpQ9dRbial7qQn8jkKt4Ys5Af-iDF1dwg6urtteCcS9VCTC40DcpBveP05Dks84ISLsiSTEZs5zbN07YvbrdH_exPGd5TWR2fRdA"
private var refreshToken = "AQAyeCRBjedVszwhRUBjAAh1z0w_S5Oru0_S3r8Vj0QuRzBnTEFw53GZet9kiHHrbhVdCvLR1nm7NXtvFHzyYmeBvIzWx8vMnJDOnMTjSeFAAOGr2Yoj8EmKLKkmLFvCKHYBCQ"
private val redirectUri = SpotifyHttpManager.makeUri("http://localhost:8080/callback")

@Component
class SpotifyClient {
    private val spotifyApi = SpotifyApi
            .Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .setRedirectUri(redirectUri)
            .setAccessToken(accessToken).setRefreshToken(refreshToken)
            .build()

    @Scheduled(fixedDelay = 50000)
    fun refreshTokenLoop() {
        val credentials = spotifyApi.authorizationCodeRefresh(clientId, clientSecret, refreshToken).build().execute()
        accessToken = credentials.accessToken
    }

    fun startPlaylist(playlist: Playlist, random: Boolean = false) {
        spotifyApi.toggleShuffleForUsersPlayback(random)
        spotifyApi.startResumeUsersPlayback(playlist.playlistContext)
    }

    fun stopPlaying() {
        spotifyApi.pause()
    }
}