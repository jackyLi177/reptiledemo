package com.common;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author liyongjie
 * @date 2018年5月15日
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api(){

        //添加head参数start
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("Authorization").description("令牌").modelRef(new ModelRef("string")).parameterType("header").required(false).build();
        pars.add(tokenPar.build());
        //添加head参数end

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("lalala")
                        .description("springboot learning")
                        .version("2.0")
                        .build())
                .select()//哪些路径和api会生成document
                .apis(RequestHandlerSelectors.any()) //监控哪些api，这里是监控所有api
                .paths(PathSelectors.any())//监控哪些路径，这里是监控所有路径
                .build()
                .globalOperationParameters(pars);
    }}
