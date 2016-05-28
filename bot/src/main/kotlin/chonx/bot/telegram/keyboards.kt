package chonx.bot.telegram

import chonx.bot.telegram.types.KeyboardButton
import chonx.bot.telegram.types.ReplyKeyboardMarkup

class KeyboardBuilder internal constructor() {

  private val rows: MutableList<List<KeyboardButton>> = mutableListOf()

  public var resize_keyboard: Boolean? = null
  public var one_time_keyboard: Boolean? = null
  public var selective: Boolean? = null

  fun row(init: RowBuilder.() -> Unit) {
    val rowBuilder = RowBuilder()
    rowBuilder.init()
    rows.add(rowBuilder.build())
  }

  internal fun build(): ReplyKeyboardMarkup = ReplyKeyboardMarkup(
      rows, resize_keyboard, one_time_keyboard, selective)
}

class RowBuilder internal constructor() {
  private val row: MutableList<KeyboardButton> = mutableListOf()

  operator fun String.unaryPlus() {
    row.add(KeyboardButton(this))
  }

  internal fun build(): List<KeyboardButton> = row
}

fun keyboard(init: KeyboardBuilder.() -> Unit): ReplyKeyboardMarkup {
  val builder = KeyboardBuilder()
  builder.init()
  return builder.build()
}
