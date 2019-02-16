package com.whats_app.kotlinwhatsapp.models

class FriendlyMessage {

    // variables
    var id: String? = null
    var text: String? = null
    var name: String? = null

    constructor()

    constructor(id: String?, text: String?, name: String?) {
        this.id = id
        this.text = text
        this.name = name
    }


}