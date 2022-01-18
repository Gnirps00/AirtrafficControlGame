package air_traffic_control_game

import org.junit.Test
import org.junit.Assert._

class AirportTest {
  
  val airport = new Airport("Malmi", 1000, 600, true)
  val r1 = new Runway("04R", 2000)
  val r2 = new Runway("33", 3000)
  val input1 = "04r"
  val input2 = "33"
  
  @Test def testRunwaysAtAirport = {
    assertTrue("Vector containing runways should be empty in the beginning.", airport.runways.isEmpty)
    
    airport.setRunway(r1)
    airport.setRunway(r2)
    assertTrue("Vector containing runways should not be empty after setting a runway.", airport.runways.nonEmpty)
    assertTrue("Vector contained wrong runway: " + airport.runways.head.name, airport.runways.head == r1)
    assertTrue("Vector contained wrong runway: " + airport.runways(1).name, airport.runways(1) == r2)
    assertTrue("Method didn't find the runway: " + airport.runways.head.name, airport.runwayFound(input1))
    assertTrue("Method didn't find the runway: " + airport.runways(1).name, airport.runwayFound(input2))
    
    assertTrue("The runway shouldn't be used", !airport.isUsing(input1))
    airport.useRunway(input2)
    assertTrue("The runway should be used", airport.isUsing(input2))
    airport.removeFromRunway(input2)
    assertTrue("The runway shouldn't be used", !airport.isUsing(input2))
  }
}