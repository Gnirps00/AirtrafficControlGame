package air_traffic_control_game

import org.junit.Test
import org.junit.Assert._

class RunwayTest {
  val runway = new Runway("04R", 2000)
  
  @Test def testUse = {
    runway.use()
    assertTrue("The state of using didn't change to true.", runway.isUsing)
  }
  
  @Test def testRemove = {
    runway.use()
    runway.remove()
    assertTrue("The state of using didn't change to false", !runway.isUsing)
  }
}