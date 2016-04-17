package com.phc.marvelapp.injection.scope;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Horatiu
 */
@Scope
@Retention(RetentionPolicy.RUNTIME)
public @interface PerActivity {}
