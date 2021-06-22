package com.todoclean;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/use-case-test/resources",
        plugin = { "pretty", "html:target/cucumber" },
        glue = "com.todoclean.adapters"
)
public class UseCaseTestsRunner {

}