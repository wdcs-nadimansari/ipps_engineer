package com.webclues.IPPSEngineer.Modelclass

class ReportResponseItem {

     var MachineName: String? = null
     var Machineoperator: String? = null

    constructor(MachineName: String, Machineoperator: String) {
        this.MachineName = MachineName
        this.Machineoperator = Machineoperator

    }
}