package chonx.core

import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class GameTest {
  @Rule @JvmField val expect = ExpectedException.none()

  val hannes = Player("hannes")
  val felix = Player("felix")
  val players = listOf(hannes, felix)
  val die: Die = TestDie()

  @Test fun `should move to next player after move`() {
    val game = Game(players, hannes)
    assertThat(game.currentPlayer).isEqualTo(hannes)

    val nextGame = game.move(game.roll(die), Slot.ACES)
    assertThat(nextGame.currentPlayer).isEqualTo(felix)

    val thirdGame = nextGame.move(nextGame.roll(die), Slot.CHANCE)
    assertThat(thirdGame.currentPlayer).isEqualTo(hannes)
  }

  @Test fun `should not allow move if player has filled the slot already`() {
    val game = Game(listOf(hannes), hannes)
    val move = game.roll(die)
    val updatedGame = game.move(move, Slot.ACES)

    assertThat(updatedGame.isLegalMove(hannes, Slot.ACES)).isFalse()

    expect.expect(IllegalMoveException::class.java)
    updatedGame.move(updatedGame.roll(die), Slot.ACES)
  }

  @Test fun `should not allow player to take turn whos turn it is not`() {
    val game = Game(players, hannes)
    val move = MoveInProgress.start(felix)

    assertThat(game.isLegalMove(move.player, Slot.ACES)).isFalse()

    expect.expect(IllegalMoveException::class.java)
    game.move(move, Slot.ACES)
  }

  @Test fun `game should end`() {
    var game = Game.new(players.toSet())
    assertThat(game.hasEnded()).isFalse()

    Slot.values().forEach { slot ->
      players.forEach {
        game = game.move(game.roll(die), slot)
      }
    }

    assertThat(game.hasEnded()).isTrue()
  }
}
