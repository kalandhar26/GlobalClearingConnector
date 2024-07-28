package com.jpm.gcc.e2e.embedded.orches;

import com.jpm.gcc.e2e.embedded.spring.MainApplication;
import com.jpm.gcc.e2e.embedded.spring.ServiceRunner;

public class OrchestrationRunner extends ServiceRunner {

    public OrchestrationRunner(){
        super(MainApplication.class, OrchestrationConfig.class);
    }
}
