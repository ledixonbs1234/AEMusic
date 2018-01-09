package com.xon.aemusic.Model

/**
 * Created by Administrator on 12/11/2017.
 */
class DataWebModel {
    val urlWeb: String
    var urlImage: String
    val title: String

    constructor(urlImage: String, urlWeb: String, title: String) {
        this.urlImage = urlImage
        this.urlWeb = urlWeb
        this.title = title
    }

}