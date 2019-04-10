package edu.iis.mto.staticmock.reader;

import edu.iis.mto.staticmock.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class})
public class NewsLoaderTest {

    @Mock
    private NewsLoader newsLoader;
    @Mock
    private ConfigurationLoader configurationLoader;
    @Mock
    private NewsReaderFactory newsReaderFactory;
    @Mock
    private NewsReader newsReader;

    private IncomingNews incomingNews;

    @Before
    public void init() {
        newsLoader = new NewsLoader();
        incomingNews = new IncomingNews();

        mockStatic(ConfigurationLoader.class);
        configurationLoader = mock(ConfigurationLoader.class);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
        when(configurationLoader.loadConfiguration()).thenReturn(new Configuration());

        newsReader = mock(NewsReader.class);
        when(newsReader.read()).thenReturn(incomingNews);

        mockStatic(NewsReaderFactory.class);
        newsReaderFactory = mock(NewsReaderFactory.class);
        when(NewsReaderFactory.getReader(Mockito.any())).thenReturn(newsReader);
    }

    @Test
    public void loadNewsShouldLoadConfigurationOnceTest() {
        newsLoader.loadNews();
        verify(configurationLoader, times(1)).loadConfiguration();
    }
}
