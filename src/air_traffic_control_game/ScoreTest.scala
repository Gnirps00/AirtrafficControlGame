package air_traffic_control_game

import org.junit.Test
import org.junit.Assert._

class ScoreTest {
  
  val player = new Player("Test")
  
  @Test def testEasy = {
    val score = new Score(player, "Easy")
    assertTrue("Initial point should be 0, but was " + score.points, score.points == 0)
    score.succeededT()
    assertTrue("The point should be 1 after succeeded take-off, but was " + score.points, score.points == 1)
    score.succeededL()
    assertTrue("The point should be 3 after succeeded landing, but was " + score.points, score.points == 3)
    score.crashed()
    assertTrue("The point should be -7 after succeeded take-off, but was " + score.points, score.points == -7)
  }
  
  @Test def testNormal = {
    val score = new Score(player, "Normal")
    assertTrue("Initial point should be 0, but was " + score.points, score.points == 0)
    score.succeededT()
    assertTrue("The point should be 1 after succeeded take-off, but was " + score.points, score.points == 1)
    score.succeededL()
    assertTrue("The point should be 5 after succeeded landing, but was " + score.points, score.points == 5)
    score.crashed()
    assertTrue("The point should be -15 after succeeded take-off, but was " + score.points, score.points == -15)
  }
  
  @Test def testHard = {
    val score = new Score(player, "Hard")
    assertTrue("Initial point should be 0, but was " + score.points, score.points == 0)
    score.succeededT()
    assertTrue("The point should be 1 after succeeded take-off, but was " + score.points, score.points == 1)
    score.succeededL()
    assertTrue("The point should be 11 after succeeded landing, but was " + score.points, score.points == 11)
    score.crashed()
    assertTrue("The point should be -39 after succeeded take-off, but was " + score.points, score.points == -39)
  }
  
}