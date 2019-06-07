package edu.iis.mto.staticmock;

import edu.iis.mto.staticmock.reader.NewsReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
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
    private PublishableNews publishableNews;

    @Before
    public void init(){

        incomingNews = new IncomingNews();

        incomingNews.add(new IncomingInfo("A",SubsciptionType.A));
        incomingNews.add(new IncomingInfo("B",SubsciptionType.B));
        incomingNews.add(new IncomingInfo("NONE",SubsciptionType.NONE));

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
    public void testLoadNewsLoadConfigurationOnce() {
        newsLoader.loadNews();
        verify(configurationLoader, times(1)).loadConfiguration();
    }

    @Test
    public void testPublishableNewsSeparatedSubsciption(){
        publishableNews = newsLoader.loadNews();

        List<String> publish = Whitebox.getInternalState(publishableNews,"publicContent");
        List<String> subscribed = Whitebox.getInternalState(publishableNews, "subscribentContent");

        Assert.assertThat(publish.size(), is(equalTo(1)));
        Assert.assertThat(subscribed.size(), is(equalTo(2)));
    }

    @Test
    public void testPublishableNewsContainsRightInformation(){
        publishableNews = newsLoader.loadNews();

        List<String> publish = Whitebox.getInternalState(publishableNews,"publicContent");
        List<String> subscribed = Whitebox.getInternalState(publishableNews, "subscribentContent");

        Assert.assertThat(publish.get(0), is(equalTo("NONE")));
        Assert.assertThat(subscribed.get(0), is(equalTo("A")));
        Assert.assertThat(subscribed.get(1), is(equalTo("B")));
    }
}
