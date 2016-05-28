package chonx.bot

import chonx.bot.telegram.Telegram
import chonx.bot.telegram.TelegramApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

fun main(args: Array<String>) {
  val botToken = System.getProperty("chonx.bot")
  val telegram = Telegram.fromToken(botToken)

  println("Listening for updates...")

  val runner = BotRunner(telegram)
  val echobot = EchoBot(telegram, runner)
  echobot.start()
}

