package chonx.cli.phases

interface Phase {
  fun preCmd(): PhaseResult
  fun handleCmd(cmd: String): PhaseResult
}
