package com.ila.checkmatecentral;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.test.context.support.WithMockUser;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@WithMockUser(username = "admin", password = "password", roles = "ADMIN")
public @interface WithMockAdmin { }
