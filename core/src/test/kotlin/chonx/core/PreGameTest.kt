package chonx.core

import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.*
import org.junit.Test

class PreGameTest {
  @Test fun `should start without players`() {
    val preGame = PreGame.new()
    assertThat(preGame.players()).isEmpty()
  }

  @Test fun `should add player`() {
    val preGame = PreGame.new()
    val newPreGame = preGame.addPlayer("Hannes")

    assertThat(newPreGame.players()).containsExactly(Player("Hannes"))
  }
}
