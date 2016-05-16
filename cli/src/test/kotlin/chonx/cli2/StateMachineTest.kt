package chonx.cli2

import chonx.core.Game
import chonx.core.Player
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class StateMachineTest {
  @Rule @JvmField val expect = ExpectedException.none()

  @Test fun `should throw if the command isnt legal`() {
    val game = Game.new(setOf(Player("Hannes")))
    val state = StateMachine(Phase.InMove(game, game.roll()))

    expect.expect(IllegalStateException::class.java)
    state.handle(Command.AddPlayer("Dieter"))
  }

  @Test fun `it should add a player`() {
    val state = StateMachine.new().handle(Command.AddPlayer("Hannes"))

    assertThat((state.phase as Phase.CollectPlayers).preGame.players())
        .containsExactly(Player("Hannes"))
  }
}
