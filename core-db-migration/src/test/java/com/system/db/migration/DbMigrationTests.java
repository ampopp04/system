package com.system.db.migration;


import com.system.util.validation.ValidationUtils;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(DbMigrationApplication.class)
public class DbMigrationTests {

    @Autowired
    private JdbcTemplate template;

    @Test
    public void testDefaultSettings() throws Exception {
        ValidationUtils.assertGreaterThan(this.template
                .queryForObject("SELECT COUNT(*) from \"schema_version\"", Integer.class), 0);
    }

}
