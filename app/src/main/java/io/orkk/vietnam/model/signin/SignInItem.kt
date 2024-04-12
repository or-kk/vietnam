package io.orkk.vietnam.model.signin

data class SignInItem(
    var menuVer: Double = 0.0,
    var caddyNumber: Int = 0,
    var cartNumber: Byte = 0,
    var powerOn: Byte = 0,
    var macAddress: String? = null
)