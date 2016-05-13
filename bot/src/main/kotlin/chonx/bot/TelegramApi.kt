package chonx.bot

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Since Telegram wants colons in their paths, we use the ./ workaround throughout
 * this interface.
 * See: https://github.com/square/retrofit/issues/1253#issuecomment-158127707
 */
interface TelegramApi {
  @GET("./bot{Token}/getMe")
  fun getMe(@Path("Token") token: String): Call<Result<User>>
}
