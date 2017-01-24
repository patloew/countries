package com.tailoredapps.template.data.local;

import com.tailoredapps.template.injection.scopes.PerApplication;

import javax.inject.Inject;

@PerApplication
public class MyRepoImpl implements MyRepo {

    @Inject
    public MyRepoImpl() { }

}
