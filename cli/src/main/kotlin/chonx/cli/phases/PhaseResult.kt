package chonx.cli.phases

sealed class PhaseResult {
  object Ready : PhaseResult()
  object Quit : PhaseResult()
  class NextPhase(val phase: Phase) : PhaseResult()
}

