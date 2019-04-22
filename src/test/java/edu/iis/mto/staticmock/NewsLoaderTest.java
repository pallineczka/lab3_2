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
import static org.junit.Assert.*;
import org.hamcrest.Matchers;
import org.junit.Assert;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.*;


@RunWith(PowerMockRunner.class)
@PrepareForTest({ConfigurationLoader.class, NewsReaderFactory.class})

public class NewsLoaderTest {
    @Mock
    private ConfigurationLoader configurationLoader;
    @Mock
    private NewsReaderFactory newsReaderFactory;
    @Mock
    private NewsLoader newsLoader;
    @Mock
    private NewsReader newsReader;

    private IncomingNews incomingNews;
    private PublishableNews publishableNews;

    @Before
    public void init(){
        newsLoader = new NewsLoader();
        incomingNews = new IncomingNews();
        mockStatic(ConfigurationLoader.class);
        mockStatic(NewsReaderFactory.class);
        configurationLoader = mock(ConfigurationLoader.class);
        newsReader = mock(NewsReader.class);
        newsReaderFactory = mock(NewsReaderFactory.class);
        when(ConfigurationLoader.getInstance()).thenReturn(configurationLoader);
        when(configurationLoader.loadConfiguration()).thenReturn(new Configuration());
        when(newsReader.read()).thenReturn(incomingNews);
        when(NewsReaderFactory.getReader(Mockito.any())).thenReturn(newsReader);
        incomingNews.add(new IncomingInfo("Subscription type A",SubsciptionType.A));
        incomingNews.add(new IncomingInfo("Subscription type B",SubsciptionType.B));
        incomingNews.add(new IncomingInfo("Subscription type C",SubsciptionType.C));
        incomingNews.add(new IncomingInfo("Subscription type NONE",SubsciptionType.NONE));
        incomingNews.add(new IncomingInfo("Subscription type NONE 2",SubsciptionType.NONE));
    }

    @Test
    public void testLoadNewsLoadConfigurationShouldBeCalledOnce() {
        newsLoader.loadNews();
        verify(configurationLoader, times(1)).loadConfiguration();
    }

    @Test
    public void testPublishAndSubscribedLists(){
        publishableNews = newsLoader.loadNews();
        List<String> publish = Whitebox.getInternalState(publishableNews,"publicContent");
        List<String> subscribed = Whitebox.getInternalState(publishableNews, "subscribentContent");
        Assert.assertThat(publish.size(), is(equalTo(2)));
        Assert.assertThat(subscribed.size(), is(equalTo(3)));
    }

    @Test
    public void testPublishAndSubscribeListShouldReturnCorrectValues(){
        publishableNews = newsLoader.loadNews();
        List<String> publish = Whitebox.getInternalState(publishableNews,"publicContent");
        List<String> subscribed = Whitebox.getInternalState(publishableNews, "subscribentContent");
        Assert.assertThat(publish.get(0), is(equalTo("Subscription type NONE")));
        Assert.assertThat(publish.get(1), is(equalTo("Subscription type NONE 2")));
        Assert.assertThat(subscribed.get(0), is(equalTo("Subscription type A")));
        Assert.assertThat(subscribed.get(1), is(equalTo("Subscription type B")));
        Assert.assertThat(subscribed.get(2), is(equalTo("Subscription type C")));
    }
}