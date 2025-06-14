package gae.piaz.boot.virtual.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootVersion;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @GetMapping("/")
    public String getConfigs() {
        long startTime = System.currentTimeMillis();
        jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        long endTime = System.currentTimeMillis();
        
        return String.format("<h1>-SpringBoot Version: %s <br/>-Java: %s <br/>-Thread %s <br/>-HikariCP Pool Size: %s <br/>-DB Connection Time: %dms</h1>", 
            SpringBootVersion.getVersion(),
            System.getProperty("java.version"), 
            Thread.currentThread(),
            System.getenv("HIKARICP_CONNECTION_POOL"),
            endTime - startTime);
    }

}