package chonx.statemachine

import chonx.core.DiceRoll
import chonx.core.Game
import chonx.core.Move
import chonx.core.Player
import chonx.core.Slot
import chonx.core.TestDie
import com.google.common.truth.Truth.assertThat
import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import chonx.statemachine.Command
import chonx.statemachine.Phase
import chonx.statemachine.StateMachine

class StateMachineTest {
  @Rule @JvmField val expect = ExpectedException.none()
  val initial = StateMachine.new()
  val testDie = TestDie()
  val gameWithPlayers = Game.new(setOf(Player("Hannes"), Player("Felix")))

  @Test fun `should throw if the command isnt legal`() {
    val game = Game.new(setOf(Player("Hannes")))
    val state = StateMachine(Phase.InMove(game, game.roll(testDie)), testDie)

    expect.expect(IllegalStateException::class.java)
    state.handle(Command.AddPlayer("Dieter"))
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
    val moveInProgress = gameWithPlayers.roll(testDie)
    val phase: Phase.InMove = StateMachine(Phase.InMove(gameWithPlayers, moveInProgress), testDie)
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
    val moveInProgress = gameWithPlayers.roll(testDie).lock(0).lock(2)
    val phase: Phase.InMove = StateMachine(Phase.InMove(gameWithPlayers, moveInProgress), testDie)
        .handle(Command.UnlockDice(listOf(2)))
        .phase()

    assertThat(phase.moveInProgress.isLocked(0)).isTrue()
    assertThat(phase.moveInProgress.isLocked(2)).isFalse()
  }

  @Test fun `it should roll from InGame`() {
    val phase: Phase.InMove = StateMachine(Phase.InGame(gameWithPlayers), testDie)
        .handle(Command.RollDice())
        .phase()

    assertThat(phase.moveInProgress.player).isEqualTo(gameWithPlayers.currentPlayer)
  }

  @Test fun `it should roll from InMove`() {
    val initialMove = gameWithPlayers.roll(testDie)
    testDie.result = 3
    val phase: Phase.InMove = StateMachine(Phase.InMove(gameWithPlayers, initialMove), testDie)
        .handle(Command.RollDice())
        .phase()

    assertThat(phase.moveInProgress.dice().toSet()).isNotEqualTo(initialMove.dice().toSet())
  }

  @Test fun `it should allow accepting dice`() {
    val move = gameWithPlayers.roll(testDie)
    val movePhase = Phase.InMove(gameWithPlayers, move)
    val resultPhase: Phase.PickSlot = StateMachine(movePhase, testDie)
        .handle(Command.AcceptDice())
        .phase()

    assertThat(resultPhase.moveInProgress).isEqualTo(move)
  }

  @Test fun `it should automatically move to PickSlot after the last roll`() {
    var move = gameWithPlayers.roll(testDie)
    while (move.rollsLeft >= 2) {
      move = move.roll() // 1 roll left now
    }
    val movePhase = Phase.InMove(gameWithPlayers, move)
    val resultPhase: Phase.PickSlot = StateMachine(movePhase, testDie)
        .handle(Command.RollDice())
        .phase()

    assertThat(resultPhase.moveInProgress.rollsLeft).isEqualTo(0)
  }

  @Test fun `it should pick a slot and move to InGame`() {
    val player = gameWithPlayers.currentPlayer
    val move = gameWithPlayers.roll(testDie)
    val initialPhase = Phase.PickSlot(gameWithPlayers, move)
    val nextPhase: Phase.InGame = StateMachine(initialPhase, testDie)
        .handle(Command.PickSlot(Slot.CHANCE))
        .phase()

    assertThat(nextPhase.game.score(player)).isGreaterThan(0)
  }

  @Test fun `it should end the game when all slots are filled`() {
    val player = Player("Hannes")
    val almostAllMoves = Slot.values()
        .filter { it != Slot.CHANCE }
        .map { Move(player, it, DiceRoll(1, 1, 1, 1, 1)) }
    val almostFinishedGame = Game(listOf(player), player, almostAllMoves)
    val initialPhase = Phase.PickSlot(almostFinishedGame, almostFinishedGame.roll(testDie))
    val nextPhase: Phase.Ended =
        StateMachine(initialPhase, testDie).handle(Command.PickSlot(Slot.CHANCE)).phase()

    assertThat(nextPhase.game.hasEnded()).isTrue()
    assertThat(nextPhase.game.score(player)).isGreaterThan(0)
    assertThat(nextPhase.game.getSlotOptions(player)).isEmpty()
  }
}
