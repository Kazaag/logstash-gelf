package biz.paluch.logging.gelf.logback;

import java.net.URL;

import biz.paluch.logging.gelf.intern.GelfMessage;
import ch.qos.logback.classic.Logger;
import org.hamcrest.core.StringContains;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.MDC;

import biz.paluch.logging.gelf.GelfTestSender;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * @author <a href="mailto:tobiassebastian.kaefer@1und1.de">Tobias Kaefer</a>
 * @since 2013-10-07
 */
public class GelfLogbackAppenderTest extends AbstractGelfLogAppenderTest
{

    @Before
    public void before() throws Exception
    {
        lc = new LoggerContext();
        JoranConfigurator configurator = new JoranConfigurator();
        configurator.setContext(lc);

        URL xmlConfigFile = getClass().getResource("/logback-gelf.xml");

        configurator.doConfigure(xmlConfigFile);

        GelfTestSender.getMessages().clear();

        MDC.remove("mdcField1");
    }

    @Test
    public void testOriginHostEmpty() throws Exception
    {

        Logger logger = lc.getLogger(getClass());

        logger.info("Blubb Test");
        assertEquals(1, GelfTestSender.getMessages().size());

        GelfMessage gelfMessage = GelfTestSender.getMessages().get(0);

        String json = gelfMessage.toJson();
        assertThat(json, containsString("\"host\":\"unknown\""));
    }
}
