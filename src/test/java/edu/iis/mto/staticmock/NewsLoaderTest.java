package edu.iis.mto.staticmock;

import edu.iis.mto.staticmock.reader.NewsReader;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest( {
        ConfigurationLoader.class,
        NewsReaderFactory.class
})
public class NewsLoaderTest {

    private ConfigurationLoader mockConfigurationLoader;
    private Configuration config;
    private IncomingNews incomingNews;
    private NewsReader testNewsReader;
    private NewsLoader newsLoader;

    @Before
    public void setUp() {


    }

}