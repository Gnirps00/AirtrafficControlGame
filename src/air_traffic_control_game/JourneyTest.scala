package air_traffic_control_game

import org.junit.Test
import org.junit.Assert._

class JourneyTest {
  
  val order = 1
  val origin = new Airport("Helsinki-Vantaa", 800, 500, true)
  val destination = new Airport("Tokyo-Narita", 0, 0, false)
  val journey = new Journey(order, origin, destination)
  
  @Test def testJourney = {
    assertTrue("The status of succeeded should be None in the beginning.", journey.succeeded.isEmpty)
    
    journey.success()
    assertTrue("The status of succeeded should be Some(true)", journey.succeeded.exists(b => b))
    
    journey.failure()
    assertTrue("The status of succeeded should be Some(false)", !journey.succeeded.forall(b => b))
  }
  
}