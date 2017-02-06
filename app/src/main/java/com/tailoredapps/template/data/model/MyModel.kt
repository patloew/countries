package com.tailoredapps.template.data.model

import paperparcel.PaperParcel
import paperparcel.PaperParcelable

@PaperParcel
class MyModel : PaperParcelable {
    companion object {
        val CREATOR = PaperParcelMyModel.CREATOR;
    }
}
