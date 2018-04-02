package com.rsk

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.rsk.security.ArgumentInitializers
import com.rsk.security.ArgumentType
import com.rsk.security.ParseArgs
import com.sun.corba.se.spi.presentation.rmi.StubAdapter.request
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.channels.consumeEach
import kotlinx.coroutines.experimental.channels.produce
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import okhttp3.*
import java.io.IOException
import java.time.format.DateTimeFormatter


fun main(args: Array<String>) {

    ParseArgs.setupDefaultValues(
            arrayOf(ArgumentInitializers("username", ArgumentType.StringType(), "-u"),
                    ArgumentInitializers("password", ArgumentType.StringType(), "-p")));

    if (args.size < 2) {
        println("usage: Program -u username -p password")
        return
    } else {
        //DoWork(args)

        DoWorkProducerConsumer(args)
    }
}

private fun DoWork(args: Array<String>) = runBlocking {
    val channel = Channel<Repo>(20)

    ParseArgs(args)

    val gh = GitHubConnector()
    val user = gh.login(ParseArgs.get("username").toString(), ParseArgs.get("password").toString())
    val job = launch {
        var repos = gh.getRepositoriesForUser(ParseArgs.get("username").toString(), ParseArgs.get("password").toString(), user.login!!)

        repos.forEach { it ->
            println("send")
            channel.send(it)
        }
        channel.close()
    }

    launch {
        for (repo in channel) {
            println(repo)
        }
    }

    job.join()
}

private fun DoWorkProducerConsumer(args: Array<String>) = runBlocking {
    val channel = Channel<Repo>(20)

    ParseArgs(args)

    val gh = GitHubConnector()
    val user = gh.login(ParseArgs.get("username").toString(), ParseArgs.get("password").toString())

    val repos = produce {
        var repos = gh.getRepositoriesForUser(ParseArgs.get("username").toString(), ParseArgs.get("password").toString(), user.login!!)

        repos.forEach { it ->
            println("send")
            send(it)
        }
    }

    val job = launch {
        repos.consumeEach { println(it) }
    }

    job.join()

}


class GitHubConnector(val baseUrl: String = "https://api.github.com/") {

    val mapper = jacksonObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)


    fun login(userName: String, password: String): UserModel {

        val credential = Credentials.basic(userName, password)

        var client = OkHttpClient.Builder().build()

        val request = Request.Builder()
                .header("Authorization", credential)
                .url("${baseUrl}user")
                .build()

        val response = client.newCall(request).execute()
        if (response.isSuccessful) {
            for (item in response.headers().toMultimap()) {
                println("${item.key}: ${item.value}")
            }
        }
        var json = response.body()?.string()!!

        val usermodel = mapper.readValue<UserModel>(json)

        println(usermodel)
        println(json)
        return usermodel;
    }

    fun getRepositoriesForUser(userName: String, password: String, name: String): Array<Repo> {

        val credential = Credentials.basic(userName, password)

        var client = OkHttpClient.Builder().build()

        val request = Request.Builder()
                .header("Authorization", credential)
                .url("${baseUrl}users/$name/repos")
                .build()

        val response = client.newCall(request).execute()
        var json = response.body()?.string()!!

        var repos: Array<Repo> = mapper.readValue(json)

        return repos
    }

}

data class UserModel(
        val login: String?,
        val id: String?,
        val name: String?,
        val password: String?,
        val avatarUrl: String?,
        val location: String?,
        val email: String?,
        val blog: String?,
        val followers: String?,
        val following: String?,
        val created_at: String?,
        val joined: String?
)

data class Repo(val id: String?, val name: String?)