package com.example.springBootWithRealcode.service.eligibility;

import com.example.springBootWithRealcode.model.ValidationConstraints;
import com.example.springBootWithRealcode.service.ValidationContext;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseValidation {

    protected ValidationContext validationContext;
    private final String code;
    private final String message;
    private final boolean hardStop;
    private final String otherInfo;

    protected abstract boolean check();

    public ValidationConstraints run(){
        return check()? ValidationConstraints.builder()
                .code(code)
                .label(message)
                .hardStop(hardStop)
                .otherInfo(otherInfo)
                .build() : null;
    }

    public boolean isHardStop() {
        return this.hardStop;
    }
}
