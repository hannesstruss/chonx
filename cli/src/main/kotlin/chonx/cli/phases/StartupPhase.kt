package chonx.cli.phases

class StartupPhase : Phase {
  override fun preCmd(): PhaseResult {
    println("")
    println("""
 @@@@@@@  @@@  @@@   @@@@@@   @@@  @@@  @@@  @@@
@@@@@@@@  @@@  @@@  @@@@@@@@  @@@@ @@@  @@@  @@@
!@@       @@!  @@@  @@!  @@@  @@!@!@@@  @@!  !@@
!@!       !@!  @!@  !@!  @!@  !@!!@!@!  !@!  @!!
!@!       @!@!@!@!  @!@  !@!  @!@ !!@!   !@@!@!
!!!       !!!@!!!!  !@!  !!!  !@!  !!!    @!!!
:!!       !!:  !!!  !!:  !!!  !!:  !!!   !: :!!
:!:       :!:  !:!  :!:  !:!  :!:  !:!  :!:  !:!
 ::: :::  ::   :::  ::::: ::   ::   ::   ::  :::
 :: :: :   :   : :   : :  :   ::    :    :   ::

             Welcome to C H O N X!
""")
    return PhaseResult.NextPhase(PreGamePhase())
  }

  override fun handleCmd(cmd: String): PhaseResult {
    throw AssertionError("StartupPhase doesn't handle commands.")
  }
}
