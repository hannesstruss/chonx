package chonx.bot

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

fun main(args: Array<String>) {
  val okClient = OkHttpClient.Builder().followRedirects(false).build()

  val api = Retrofit.Builder()
      .baseUrl("https://api.telegram.org/")
      .client(okClient)
      .addConverterFactory(ScalarsConverterFactory.create())
      .addConverterFactory(MoshiConverterFactory.create())
      .build()
      .create(TelegramApi::class.java)

  val botToken = System.getProperty("chonx.bot")
  val telegram = Telegram(api, botToken)

  telegram.getUpdates().forEach { println(it) }
}

