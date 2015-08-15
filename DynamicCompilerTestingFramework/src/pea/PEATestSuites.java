package pea;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ MustEscape.class, NotEscape.class, PartialEscape.class })
public class PEATestSuites {
}
