package com.webserg.travelui.ui;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.script.ScriptTemplateConfigurer;
import org.springframework.web.servlet.view.script.ScriptTemplateViewResolver;

@Configuration
@EnableWebMvc
public class WebConfig {
    @Bean
    public ScriptTemplateConfigurer handleConfigurer(){
        ScriptTemplateConfigurer configurer = new ScriptTemplateConfigurer();
        configurer.setEngineName("nashorn");
        configurer.setScripts("/static/polyfill.js",
                "/static/render.js",
                "/META-INF/resources/webjars/handlebars/3.0.0-1/handlebars.js"
                );
        configurer.setRenderFunction("render");
        configurer.setSharedEngine(false);
        return configurer;
    }


    @Bean
    public ViewResolver viewResolver(){
        ScriptTemplateViewResolver viewResolver = new ScriptTemplateViewResolver();
        viewResolver.setPrefix("/templates/");
        viewResolver.setSuffix(".html");
        return viewResolver;
    }
}
