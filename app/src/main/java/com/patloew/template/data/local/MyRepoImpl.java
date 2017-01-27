package com.patloew.template.data.local;

import com.patloew.template.injection.scopes.PerApplication;

import javax.inject.Inject;

@PerApplication
public class MyRepoImpl implements MyRepo {

    @Inject
    public MyRepoImpl() { }

}
