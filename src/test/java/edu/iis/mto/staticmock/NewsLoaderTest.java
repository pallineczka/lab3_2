package edu.iis.mto.staticmock;

import edu.iis.mto.staticmock.reader.NewsReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.List;

import static org.hamcrest.EasyMock2Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.mockito.AdditionalMatchers.not;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
//import static org.mockito.Mockito.verify;
//import static org.mockito.internal.verification.VerificationModeFactory.times;
//import static org.powermock.api.mockito.PowerMockito.mockStatic;
//import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest( {
        ConfigurationLoader.class,
        NewsReaderFactory.class
})
public class NewsLoaderTest {

    @Mock
    private ConfigurationLoader mockConfigurationLoader;
    private Configuration config;
    private IncomingNews incomingNews;
    private NewsReader testNewsReader;
    private NewsLoader newsLoader;
    private PublishableNews publishableNews;


    @Before
    public void setUp() {
        mockStatic(ConfigurationLoader.class);
        when(ConfigurationLoader.getInstance()).thenReturn(mockConfigurationLoader);

        config = new Configuration();
        Whitebox.setInternalState(config, "readerType", "testType");
        when(ConfigurationLoader.getInstance().loadConfiguration()).thenReturn(config);

        incomingNews = new IncomingNews();
        incomingNews.add(new IncomingInfo("News1", SubsciptionType.NONE));
        incomingNews.add(new IncomingInfo("News2", SubsciptionType.A));
        incomingNews.add(new IncomingInfo("News3", SubsciptionType.NONE));
        incomingNews.add(new IncomingInfo("News4", SubsciptionType.B));
        incomingNews.add(new IncomingInfo("News5", SubsciptionType.C));

        testNewsReader = new NewsReader() {
            @Override
            public IncomingNews read() {
                return incomingNews;
            }
        };

        mockStatic(NewsReaderFactory.class);
        when(NewsReaderFactory.getReader("testType")).thenReturn(testNewsReader);

        newsLoader = new NewsLoader();
    }

    @Test
    public void shouldCallLoadConfigurationMethodOnceInConfigurationLoader() {
        PublishableNews result = newsLoader.loadNews();

        verify(mockConfigurationLoader, times(1)).loadConfiguration();
    }

    @Test
    public void publishableNewsShouldSeparateNewsTest() {
        publishableNews = newsLoader.loadNews();

        List<String> publish = Whitebox.getInternalState(publishableNews, "publicContent");
        List<String> subscribed = Whitebox.getInternalState(publishableNews, "subscribentContent");
        Assert.assertThat(publish.size(), is(2));
        Assert.assertThat(subscribed.size(), is(3));
    }
}