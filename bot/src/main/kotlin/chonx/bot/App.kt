package chonx.bot

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

fun main(args: Array<String>) {
  val api = Retrofit.Builder()
      .baseUrl("https://api.telegram.org/")
      .addConverterFactory(ScalarsConverterFactory.create())
      .addConverterFactory(MoshiConverterFactory.create())
      .build()
      .create(TelegramApi::class.java)

  val response = api.getMe(System.getProperty("chonx.bot")).execute()
  val message = response.body()
  println(response.raw().request())
  println(message)
}

