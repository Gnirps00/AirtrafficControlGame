package air_traffic_control_game

import org.junit.Test
import org.junit.Assert._

class AircraftTest {
  val width = 800
  val height = 500
  val airline = new Airline("Finnair", "AY")
  val number = 350
  val helsinki = new Airport("Helsinki-Vantaa", width, height, true)
  val runway = new Runway("33", 3000)
  helsinki.setRunway(runway)
  val tokyo = new Airport("Tokyo-Narita", 0, 0, false)
  val j1 = {
    val origin = tokyo
    val destination = helsinki
    new Journey(1, origin, destination)
  }
  val j2 = {
    val destination = helsinki
    val origin = tokyo
    new Journey(2, origin, destination)
  }
  val j3 = {
    val origin = helsinki
    val destination = tokyo
    new Journey(1, origin, destination)
  }
  val maxFuel = 10000
  val consumption = 20
  val passengers = 400
  val pos01 = new Vector3D(-1, -1, 10000)
  val pos02 = new Vector3D(300, 200, 10000)
  val pos03 = new Vector3D(400, 250, 0)
  val speed01 = new Vector3D(50, 20, 0)
  val speed02 = new Vector3D(50, 20, 0)
  val speed03 = new Vector3D(0, 0, 0)
  val wind = new Vector3D(15, 15, 0)
  
  val a1 = new Aircraft(airline, number, j1, maxFuel, consumption, passengers, pos01, speed01, true)
  val a2 = new Aircraft(airline, number, j2, maxFuel, consumption, passengers, pos02, speed02, true)
  val a3 = new Aircraft(airline, number, j3, maxFuel, consumption, passengers, pos03, speed03, false)
  
  
  
  @Test def testDistanceAndNear = {
    val d1 = a1.distance
    val d2 = a2.distance
    
    assertTrue("Wrong distance, expected: 473, but was " + d1.toInt, d1.toInt == 473)
    assertTrue("The aircraft " + a1.code + " should not be near.", !a1.isNear)
    assertTrue("Wrong distance, expected: 111, but was " + d2.toInt, d2.toInt == 111)
    assertTrue("The aircraft " + a2.code + " should be near.", a2.isNear)
  }
  

  @Test def testInside = {
    assertTrue("The aircraft " + a1.code + "should not be inside.", !a1.isInside(width, height))
    assertTrue("The aircraft " + a2.code + "should be inside.", a2.isInside(width, height))
  }
  
  
  @Test def testFailed = {
    assertTrue("Initial state of isFailed should be false", !a2.isFailed)
    a2.failed
    assertTrue("State of isFailed should be true", a2.isFailed)
    a2.success
    assertTrue("State of isFailed should be false", !a2.isFailed)
  }
  
  
  @Test def testStandBy = {
    assertTrue("The aircraft " + a1.code + " should not be able to stand-by.", a1.standBy("") == "The airport is too far away.")
    assertTrue("The aircraft " + a2.code + " should be able to stand-by.", a2.standBy("") == "Aircraft " + a2.code + " stand-by to land successfully.")
    assertTrue("The aircraft " + a2.code + " should not be able to stand-by.", a2.standBy("") == "This aircraft is already stand-by.")
    assertTrue("The runway should not be found.", a3.standBy("") == "To stand-by on ground, please type runway.")
    assertTrue("The runway should not be found.", a3.standBy("32") == "A runway " + "32" + " not found.")
    assertTrue("The runway should be found.", a3.standBy("33") == "The aircraft is going to use the runway " + runway.name)
  }
  
  
  @Test def testTakeOff = {
    assertTrue("The aircraft " + a1.code + " should not be able to take-off.", a1.takeOff("33") == "Flying aircraft is not able to take off.")
    assertTrue("The aircraft " + a2.code + " should not be able to take-off.", a2.takeOff("33") == "Flying aircraft is not able to take off.")
    a3.standBy("33")
    val was = a3.takeOff("32")
    assertTrue("The aircraft " + a3.code + " should not be able to take-off, but was " + was, was == "Aircraft " + a3.code + " is not using runway " + "32")
    assertTrue("The aircraft " + a3.code + " should be able to take-off.", a3.takeOff("33") == "The take-off succeeded.")
  }
  
  
  @Test def testLand = {
    assertTrue("The aircraft " + a3.code + "should not be able to land.", a3.land("33") == "Only flying aircrafts are able to land.")
    assertTrue("The aircraft " + a2.code + "should not be able to land.", a2.land("33") == "To land, the aircraft have to stand-by first.")
    a3.standBy("33")
    a2.standBy("")
    assertTrue("The aircraft " + a2.code + "should not be able to land.", a2.land("33") == "The runway is currently using. Please order the aircraft currently standing-by on the ground to take-off.")
    a3.takeOff("33")
    assertTrue("The aircraft " + a2.code + "should be able to land.", a2.land("33") == "The landing succeeded.")
  }
  
  
  @Test def testMove = {
    a1.crash()
    a1.move(wind)
    assertTrue("The aircraft " + a1.code + " should not be able to move.", a1.fuel == 10000)

    val a3Pos1 = a3.pos
    a3.move(wind)
    assertTrue("The aircraft " + a3.code + " should not move.", a3.fuel == 10000 && a3Pos1 == a3.pos)
    
    val a2Pos2 = new Vector3D(365, 235, 10000)
    a2.move(wind)
    assertTrue("The aircraft " + a2.code + " should move.", a2.fuel == 9980 && a2.pos == a2Pos2)
  }
  
}