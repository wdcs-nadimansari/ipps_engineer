package com.webclues.IPPSEngineer.Modelclass

class SensorResponseItem {
    var sensorname: String? = null
    var sensorhistory: String? = null

    constructor(sensorname: String, sensorhistory: String) {

        this.sensorname = sensorname
        this.sensorhistory = sensorhistory
    }
}