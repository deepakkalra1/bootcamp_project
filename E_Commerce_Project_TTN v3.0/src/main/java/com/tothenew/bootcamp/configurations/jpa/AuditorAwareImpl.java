package com.tothenew.bootcamp.configurations.jpa;

import com.tothenew.bootcamp.constants.SpringSeciurityUtility;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {


    @Override
    public Optional<String> getCurrentAuditor() {
        Optional<String> val = Optional.of(SpringSeciurityUtility.getUsernameViaSecurity());
        return val;

    }
}
