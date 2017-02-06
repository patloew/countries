package com.tailoredapps.template.data.local

import com.tailoredapps.template.injection.scopes.PerApplication

import javax.inject.Inject

@PerApplication
class MyRepoImpl
@Inject
constructor() : MyRepo
