package com.system.db.migration.base;

import com.system.inversion.InversionContainer;
import com.system.inversion.intercept.SimpleClasspathMetadataReaderFactory;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static com.system.manipulator.interceptor.util.InterceptorUtils.classInterceptor;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * The <class>BaseIntegrationTest</class> defines the
 * required configuration for executing tests against an integration
 * instances of the system
 *
 * @author Andrew
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = InversionContainer.class, webEnvironment = RANDOM_PORT)
public abstract class BaseIntegrationTest {
    static {
        classInterceptor(SimpleMetadataReaderFactory.class, SimpleClasspathMetadataReaderFactory.class);
    }
}
