package com.patloew.countries.data.local;

import com.patloew.countries.injection.scopes.PerApplication;

import javax.inject.Inject;

@PerApplication
public class MyRepoImpl implements MyRepo {

    @Inject
    public MyRepoImpl() { }

}
