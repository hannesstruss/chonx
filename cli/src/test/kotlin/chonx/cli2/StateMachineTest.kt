package chonx.cli2

import chonx.core.Game
import chonx.core.Player
import chonx.core.TestDie
import com.google.common.truth.Truth.assertThat
import org.junit.Assert.fail
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException

class StateMachineTest {
  @Rule @JvmField val expect = ExpectedException.none()
  val initial = StateMachine.new()
  val testDie = TestDie()
  val gameWithPlayers = Game.new(setOf(Player("Hannes"), Player("Felix")), testDie)

  @Test fun `should throw if the command isnt legal`() {
    val game = Game.new(setOf(Player("Hannes")))
    val phase = Phase.InMove(game, game.roll())

    expect.expect(IllegalStateException::class.java)
    reduce<Phase>(phase, Command.AddPlayer("Dieter"))
  }

  @Test fun `it should add a player`() {
    val phase: Phase.CollectPlayers = initial.handle(Command.AddPlayer("Hannes")).phase()
    assertThat(phase.preGame.players()).containsExactly(Player("Hannes"))
  }

  @Test fun `it should begin game`() {
    val phase: Phase.InGame = initial
        .handle(Command.AddPlayer("Hannes"))
        .handle(Command.BeginGame())
        .phase()

    assertThat(phase.game).isEqualTo(Game.new(setOf(Player("Hannes"))))
  }

  @Test fun `it should lock dice`() {
    val moveInProgress = gameWithPlayers.roll()
    val phase: Phase.InMove = StateMachine(Phase.InMove(gameWithPlayers, moveInProgress))
        .handle(Command.LockDice(listOf(0, 2)))
        .phase()

    assertThat(phase.moveInProgress.isLocked(0)).isTrue()
    assertThat(phase.moveInProgress.isLocked(1)).isFalse()
    assertThat(phase.moveInProgress.isLocked(2)).isTrue()
    assertThat(phase.moveInProgress.isLocked(3)).isFalse()
    assertThat(phase.moveInProgress.isLocked(4)).isFalse()
    assertThat(phase.moveInProgress.isLocked(5)).isFalse()
  }

  @Test fun `it should unlock dice`() {
    val moveInProgress = gameWithPlayers.roll().lock(0).lock(2)
    val phase: Phase.InMove = StateMachine(Phase.InMove(gameWithPlayers, moveInProgress))
        .handle(Command.UnlockDice(listOf(2)))
        .phase()

    assertThat(phase.moveInProgress.isLocked(0)).isTrue()
    assertThat(phase.moveInProgress.isLocked(2)).isFalse()
  }

  @Test fun `it should roll from InGame`() {
    val phase: Phase.InMove = StateMachine(Phase.InGame(gameWithPlayers))
        .handle(Command.RollDice())
        .phase()

    assertThat(phase.moveInProgress.player).isEqualTo(gameWithPlayers.currentPlayer)
  }

  @Test fun `it should roll from InMove`() {
    val initialMove = gameWithPlayers.roll()
    testDie.result = 3
    val phase: Phase.InMove = StateMachine(Phase.InMove(gameWithPlayers, initialMove))
        .handle(Command.RollDice())
        .phase()

    assertThat(phase.moveInProgress.dice().toSet()).isNotEqualTo(initialMove.dice().toSet())
  }

  @Test fun `it should automatically move to PickSlot after the last roll`() {
    fail()
  }

  @Test fun `it should pick a slot and move to InGame`() {
    fail()
  }

  @Test fun `it should end the game when all slots are filled`() {
    fail()
  }
}
