package com.whats_app.kotlinwhatsapp.models

class Users
{
    // variables
    var display_name : String? = null
    var image : String? = null
    var thumb_image : String? = null
    var status : String? = null


    constructor(display_name: String?, image: String?, thumb_image: String?, status: String?) {
        this.display_name = display_name
        this.image = image
        this.thumb_image = thumb_image
        this.status = status
    }


    constructor() {
    }


}