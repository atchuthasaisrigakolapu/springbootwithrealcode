package com.example.springBootWithRealcode.service.eligibility;

import com.example.springBootWithRealcode.model.EligibilityConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseEligibility {

    protected EligibilityContext eligibilityContext;
    private final String code;
    private final String message;
    private final boolean hardStop;

    protected abstract boolean check();

    public EligibilityConstraint run(){
        return check()?EligibilityConstraint.builder()
                .code(code)
                .label(message)
                .hardstop(hardStop)
                .build() : null;
    }

    public boolean isHardStop() {
        return this.hardStop;
    }
}
