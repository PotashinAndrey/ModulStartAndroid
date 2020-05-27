package com.example.potashin.data

import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

import com.google.gson.Gson

const val link: String = "http://10.0.2.2:5000/api/"

class Api {
    fun sendPostRequest(method:String, data: Any, token: String = ""): String {

//        var reqParam = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(userName, "UTF-8")
//        reqParam += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8")
        val mURL = URL(link + method)

        val jsonData = Gson().toJson(data)

        with(mURL.openConnection() as HttpURLConnection) {
            if (token.count() > 0) {
                val auth = "Bearer $token"
                setRequestProperty("Authorization", auth)
            }

            // optional default is GET
            requestMethod = "POST"

            setRequestProperty("Content-Type", "Application/json")

            val wr = OutputStreamWriter(getOutputStream());
            wr.write(jsonData);
            wr.flush();

            println("URL : $url")
            println("Response Code : $responseCode")

            BufferedReader(InputStreamReader(getInputStream())).use {
                val response = StringBuffer()

                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                println("Response : $response")
                return response.toString();
            }
        }
    }

    fun sendGetRequest(method: String, token: String = ""): String {

//        var reqParam = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(userName, "UTF-8")
//        reqParam += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8")

        val mURL = URL(link + method);

        with(mURL.openConnection() as HttpURLConnection) {
            if (token.count() > 0) {
                val auth = "Bearer $token"
                setRequestProperty("Authorization", auth)
            }

            // optional default is GET
            requestMethod = "GET"

            setRequestProperty("Content-Type", "Application/json")

            println("URL : $url")
            println("Response Code : $responseCode")

            BufferedReader(InputStreamReader(inputStream)).use {
                val response = StringBuffer()

                var inputLine = it.readLine()
                while (inputLine != null) {
                    response.append(inputLine)
                    inputLine = it.readLine()
                }
                it.close()
                println("Response : $response")

                return response.toString();
            }
        }
    }
}