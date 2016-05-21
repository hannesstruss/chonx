package chonx.statemachine

import chonx.core.Game
import chonx.core.MoveInProgress
import chonx.core.PreGame

sealed class Phase {
  class CollectPlayers(val preGame: PreGame) : Phase()

  abstract class GamePhase(val game: Game): Phase()
  abstract class MovePhase(game: Game, val moveInProgress: MoveInProgress) : GamePhase(game)
  class InGame(game: Game) : GamePhase(game)
  class InMove(game: Game, moveInProgress: MoveInProgress) : MovePhase(game, moveInProgress)
  class PickSlot(game: Game, moveInProgress: MoveInProgress) : MovePhase(game, moveInProgress)
  class Ended(game: Game) : GamePhase(game)
}
