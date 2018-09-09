package com.fedorowiat.spotify

import com.fedorowiat.playlist.Playlist
import com.wrapper.spotify.SpotifyApi
import com.wrapper.spotify.SpotifyHttpManager

private const val clientId = "eb1b3708670741cbbb51ffdb89fe53df"
private const val clientSecret = "f85f6e6c82a64a0dad7c596d5cc581db"
private const val accessToken = "BQCmW1Zr9ShQAcs3F10ynPYy8Rk2z8Rhf8ltnFgLqKRUGDmqzUsi5PU85xLmmGTUJzFrZGzwQH_w4uNZQ4sjiPfjPTwvth5CqeYnd4B8PK2AUDMF-y1Gt1qSreuw_T_eFIh7Zr7Ulh0E3mc_-1I0wUU8FpjFDnAhJlA08SPErsRAvDDouQyFjEfXK6f9Qpk_7FuNa0W477f9sU5o9ghoJBWQ6aEQef4PN_AGIHOrgtvlGmp3fmI2rG9JkrU77QCC8MFksvEGdSBUecDyQ63uYugT91Y"
private const val refreshToken = "AQBQxCo1HP8GZf972d_qP_cx23n2tbwpvul_oMB4pjtu2ukF8EHrrGxx_DHmYPyE-EtjJvDVdgs3CpvO5FDfoh2qFLwTWI1E__IUox3k9CPOLYZVgYoK0CoQCjjsHzSV0Cg2Dg"
private val redirectUri = SpotifyHttpManager.makeUri("http://localhost:8080/callback")

class SpotifyClient {
    private val spotifyApi = SpotifyApi
            .Builder()
            .setClientId(clientId)
            .setClientSecret(clientSecret)
            .setRedirectUri(redirectUri)
            .setAccessToken(accessToken).setRefreshToken(refreshToken)
            .build()

    fun startPlaylist(playlist: Playlist, random: Boolean = false) {
        spotifyApi.toggleShuffleForUsersPlayback(random)
        spotifyApi.startResumeUsersPlayback(playlist.playlistContext)
    }
}