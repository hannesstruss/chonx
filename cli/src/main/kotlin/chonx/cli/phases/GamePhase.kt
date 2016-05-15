package chonx.cli.phases

import chonx.core.Game
import chonx.core.MoveInProgress
import chonx.core.Player
import chonx.core.Slot

class GamePhase private constructor(val game: Game) : Phase {
  companion object {
    fun new(players: Set<Player>) = GamePhase(Game.new(players))

    fun move(previousGame: Game, moveInProgress: MoveInProgress, slot: Slot): GamePhase {
      val newGame = previousGame.move(moveInProgress, slot)
      return GamePhase(newGame)
    }
  }

  override fun preCmd(): PhaseResult {
    if (game.hasEnded()) {
      println("Game over")
      return PhaseResult.Quit
    }
    println("Hi! It's your turn, ${game.currentPlayer.name}!")
    return PhaseResult.NextPhase(MoveInProgressPhase(game, game.roll()))
  }

  override fun handleCmd(cmd: String): PhaseResult {
    return PhaseResult.Ready
  }
}
