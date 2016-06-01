package com.system.db.migration.base;

import com.system.inversion.InversionContainer;
import com.system.inversion.intercept.SimpleClasspathMetadataReaderFactory;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.system.manipulator.interceptor.util.InterceptorUtils.classInterceptor;

/**
 * The <class>BaseIntegrationTest</class> defines the
 * required configuration for executing tests against an integration
 * instances of the system
 *
 * @author Andrew
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(InversionContainer.class)
@WebIntegrationTest(randomPort = true)
public abstract class BaseIntegrationTest {
    static {
        classInterceptor(SimpleMetadataReaderFactory.class, SimpleClasspathMetadataReaderFactory.class);
    }
}
