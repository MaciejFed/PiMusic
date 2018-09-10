package com.fedorowiat.spotify

import com.fedorowiat.configuration.Configuration
import com.fedorowiat.configuration.ConfigurationService
import com.fedorowiat.playlist.Playlist
import com.wrapper.spotify.SpotifyApi
import com.wrapper.spotify.SpotifyHttpManager
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

private const val clientId = "eb1b3708670741cbbb51ffdb89fe53df"
private const val clientSecret = "f85f6e6c82a64a0dad7c596d5cc581db"
private val redirectUri = SpotifyHttpManager.makeUri("http://localhost:8080/callback")

@Component
class SpotifyClient(private val configurationService: ConfigurationService) {

    private val spotifyApiContainer = SpotifyApiContainer(configurationService.getConfiguration())

    @Scheduled(fixedDelay = 60000)
    fun refreshTokenLoop() {
        spotifyApiContainer.performAction { spotifyApi ->
            run {
                val credentials = spotifyApi.authorizationCodeRefresh()
                                         .build()
                                         .execute()
                spotifyApi.accessToken = credentials.accessToken
                configurationService.updateAccessToken(credentials.accessToken)
            }
        }

    }

    fun startPlaylist(playlist: Playlist, random: Boolean = true) {
        spotifyApiContainer.performAction { it.startResumeUsersPlayback(playlist.playlistContext) }
        spotifyApiContainer.performAction { it.toggleShuffle(random) }
    }

    fun stopPlaying() {
        spotifyApiContainer.performAction { it.pause() }
    }
}

class SpotifyApiContainer(configuration: Configuration) {
    private val spotifyApi = SpotifyApi
            .Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .setRedirectUri(redirectUri)
            .setAccessToken(configuration.accessToken)
            .setRefreshToken(configuration.refreshToken)
            .build()

    @Synchronized
    fun performAction(spotifyAction: (spotify: SpotifyApi) -> Unit) {
        spotifyAction(spotifyApi)
    }
}