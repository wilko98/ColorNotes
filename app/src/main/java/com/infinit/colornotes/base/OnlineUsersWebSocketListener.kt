package com.infinit.colornotes.base

import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import timber.log.Timber

class OnlineUsersWebSocketListener(val view: OnlineUsersWebSocketView) : WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        Timber.d(response.message)
        super.onOpen(webSocket, response)
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        Timber.d("web socket received message :$bytes")
        view.showOnlineUsers(bytes.utf8())
        super.onMessage(webSocket, bytes)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        view.showOnlineUsers(text)
        super.onMessage(webSocket, text)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        Timber.d("Web socket closing, code: $code reason: $reason")
        super.onClosing(webSocket, code, reason)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        Timber.d("Web socket closed, code: $code reason: $reason")
        super.onClosed(webSocket, code, reason)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Timber.d("Web socket failure  webSocket: $webSocket, throwable: $t, respnse: $response")
        super.onFailure(webSocket, t, response)
    }
}