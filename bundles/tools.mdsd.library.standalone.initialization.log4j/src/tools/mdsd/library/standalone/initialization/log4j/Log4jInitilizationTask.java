package tools.mdsd.library.standalone.initialization.log4j;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.PatternLayout;

import tools.mdsd.library.standalone.initialization.InitializationTask;
import tools.mdsd.library.standalone.initialization.StandaloneInitializationException;

public class Log4jInitilizationTask implements InitializationTask {

    @Override
    public void initilizationWithoutPlatform() throws StandaloneInitializationException {
        BasicConfigurator.resetConfiguration();
        BasicConfigurator.configure(new ConsoleAppender(new PatternLayout("%m%n")));
    }

}
