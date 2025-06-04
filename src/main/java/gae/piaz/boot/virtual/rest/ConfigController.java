package gae.piaz.boot.virtual.rest;

import org.springframework.boot.SpringBootVersion;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
public class ConfigController {

    @GetMapping("/")
    public String getConfigs() {
        return String.format("<h1>-SpringBoot Version: %s <br/>-Java: %s <br/>-Thread %s</h1>", SpringBootVersion.getVersion(),
            System.getProperty("java.version"), Thread.currentThread());
    }

}