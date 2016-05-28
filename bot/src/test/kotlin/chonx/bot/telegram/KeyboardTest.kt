package chonx.bot.telegram

import chonx.bot.telegram.types.KeyboardButton
import chonx.bot.telegram.types.ReplyKeyboardMarkup
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class KeyboardTest {
  @Test fun shouldMakeSimpleKeyboard() {
    val kb = keyboard {
    }

    assertThat(kb).isEqualTo(ReplyKeyboardMarkup(listOf()))
  }

  @Test fun shouldMakeComplexKeyboard() {
    val kb = keyboard {
      row {
        +"Cool!"
      }

      row {
        +"What"
        +"Nice"
      }

      row {
        (1..3)
            .map { "Button ${it}" }
            .forEach {
              +it
            }
      }
    }

    val buttons = listOf(
        listOf(
            KeyboardButton("Cool!")
        ),
        listOf(
            KeyboardButton("What"),
            KeyboardButton("Nice")
        ),
        listOf(
            KeyboardButton("Button 1"),
            KeyboardButton("Button 2"),
            KeyboardButton("Button 3")
        )
    )

    assertThat(kb).isEqualTo(ReplyKeyboardMarkup(buttons))
  }
}
