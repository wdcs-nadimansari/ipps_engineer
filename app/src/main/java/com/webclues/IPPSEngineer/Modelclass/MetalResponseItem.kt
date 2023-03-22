package com.webclues.IPPSEngineer.Modelclass

class MetalResponseItem {

    var metalimage: Int? = null
    var metalpriority: String? = null
    var metalname: String? = null
    var metaldate: String? = null
    var metaldesc: String? = null
    var metalstatus: String? = null
    var problemname: String? = null
    var engineername: String? = null

    constructor(
        metalimage: Int, metalpriority: String, metalname: String,
        metaldesc: String, metaldate: String, metalstatus: String,
        problemname: String, engineername: String
    ) {

        this.metalimage = metalimage;
        this.metalpriority = metalpriority;
        this.metalname = metalname;
        this.metaldesc = metaldesc
        this.metaldate = metaldate
        this.metalstatus = metalstatus
        this.problemname = problemname
        this.engineername = engineername

    }
}