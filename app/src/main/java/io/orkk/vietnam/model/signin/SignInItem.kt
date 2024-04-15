package io.orkk.vietnam.model.signin

data class SignInItem(
    var menuVer: Double = 0.0,
    var id: Int = 0,
    var cartNumber: Byte = 0,
    var powerOn: Byte = 0,
    var macAddress: String? = "T1-E2-S3-T4-P5-C6"
)