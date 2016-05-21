package statemachine

import chonx.core.Game
import chonx.core.MoveInProgress
import chonx.core.PreGame

sealed class Phase {
  class CollectPlayers(val preGame: PreGame) : Phase()
  class InGame(val game: Game) : Phase()
  class InMove(val game: Game, val moveInProgress: MoveInProgress) : Phase()
  class PickSlot(val game: Game, val moveInProgress: MoveInProgress) : Phase()
  class Ended(val game: Game) : Phase()
}
