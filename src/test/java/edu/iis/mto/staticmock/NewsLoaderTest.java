package edu.iis.mto.staticmock;

import edu.iis.mto.staticmock.reader.NewsReader;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class})

public class NewsLoaderTest {

    @Mock
    private ConfigurationLoader configurationLoader;
    @Mock
    private NewsReader newsReader;
    @Mock
    private NewsReaderFactory newsReaderFactory;
    @Mock
    private NewsLoader newsLoader;

    private IncomingNews incomingNews;

    @Before
    public void init(){

        incomingNews = new IncomingNews();
        newsLoader = new NewsLoader();

        mockStatic(ConfigurationLoader.class);
        mockStatic(NewsReaderFactory.class);

        configurationLoader = mock(ConfigurationLoader.class);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
        when(configurationLoader.loadConfiguration()).thenReturn(new Configuration());

        newsReader = mock(NewsReader.class);
        when(newsReader.read()).thenReturn(incomingNews);

        newsReaderFactory = mock(NewsReaderFactory.class);
        when(NewsReaderFactory.getReader(Mockito.any())).thenReturn(newsReader);
    }

    @Test
    public void TestLoadNewsLoadConfigurationOnce() {
        newsLoader.loadNews();
        verify(configurationLoader, times(1)).loadConfiguration();
    }

}
