package com.junli.retrofit_demo.network.domain

import java.time.LocalDateTime

data class NormalUser(
    val userId: Long,
    val userName: String,
    val nickName: String,
    val suerType: String,
    val email: String,
    val phoneNumber: String,
    val sex: String,
    val avatar: String,
    val password: String,
    val status: String,
    val delFlag: String,
    val loginIp: String,
    val loginDate: String,
    val createBy: String,
    val createTime: LocalDateTime,
    val updateBy: String,
    val updateTime: String,
    val remark: String
)
