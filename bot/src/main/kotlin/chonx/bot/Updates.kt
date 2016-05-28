package chonx.bot

import chonx.bot.telegram.Telegram
import chonx.bot.telegram.types.Update
import rx.Observable

class Updates(private val telegram: Telegram) {
  private val updates = call().share()

  companion object {
    val Timeout = 30
  }

  fun updates(): Observable<Update> {
    return updates
  }

  private fun call(offset: Long = 0): Observable<Update> {
    return Observable.fromCallable { telegram.getUpdates(offset, Timeout) }
        .flatMap { updates: List<Update> ->
          val max = updates.map { it.update_id }.max() ?: offset - 1
          val newOffset: Long = max + 1
          Observable.from(updates).concatWith(call(newOffset))
        }
  }
}
