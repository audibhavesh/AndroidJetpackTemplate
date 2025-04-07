package com.fermion.android.base.network.connection

sealed class ConnectionState {
    object Available : ConnectionState()
    object Unavailable : ConnectionState()
}