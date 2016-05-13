package chonx

import chonx.core.pairs
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class UtilsTest {
  @Test fun testPairs() {
    assertThat(listOf(1, 2, 3, 4).pairs()).isEqualTo(
        listOf(
            Pair(1, 2),
            Pair(2, 3),
            Pair(3, 4)
        ))
  }
}
