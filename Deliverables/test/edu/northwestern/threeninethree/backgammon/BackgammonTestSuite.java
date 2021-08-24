package edu.northwestern.threeninethree.backgammon;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        BoardTest.class,
        RuleCheckerTest.class,
        PlayerTest.class,
        RemotePlayerAdapterTest.class,
        //AdminTest.class, <- Commented out due to networking failures
        //TournamentTest.class <- Commented out due to networking failures
})
public class BackgammonTestSuite {
}