/**
 * Total Comfort API
 *   
 * Copyright 2015 Eric Thomas
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * 
 * CHANGELOG
 * V4 lgk - Supports celsius and fahrenheit with option, and now colors.
 * V5 lgk - Due to intermittant update failures added last update date/time tile so that you can see when it happended
 *          not there is a new input tzoffset which defaults to my time ie -5 which you must set .
 * V6 lgk -	Add support for actually knowing the fan is on or not (added tile),
 * 			and also the actual operating state ie heating,cooling or idle via new response variables.
 * V7 lgk - change the new operating state to be a value vs standard tile
 * 			to work around a bug smartthings caused in the latest 2.08 release with text wrapping.
 * 			related also added icons to the operating state, and increase the width of the last update
 * 			to avoid wrapping.
 * V8 DAW - Refactor and code cleanup.
 */
 

preferences {
  input("username",            "text",     title: "Username",                                                   required: true,                     description: "Your Total Comfort User Name")
  input("password",            "password", title: "Password",                                                   required: true,                     description: "Your Total Comfort password" )
  input("honeywelldevice",     "text",     title: "Device ID",                                                  required: true,                     description: "Your Device ID"              )
  input("enableOutdoorTemps",  "enum",     title: "Enable outdoor temperature sensor?", options: ["Yes", "No"], required: false, defaultValue: "No"                                            )
  input("tempScale",           "enum",     title: "Fahrenheit or Celsius?",             options: ["F", "C"],    required: false, defaultValue: "F"                                             )
  input("tzOffset",            "number",   title: "Time zone offset +/-xx?",                                    required: false, defaultValue: -5,  description: "Time Zone Offset ie -5."     )  
}

metadata {
  definition (name: "Total Comfort API", namespace:"Total Comfort API", author: "ET, LGK, DAW") {
    capability "Polling"
    capability "Thermostat"
    capability "Refresh"
    capability "Temperature Measurement"
    capability "Sensor"
    capability "Relative Humidity Measurement" 
       
    command    "heatLevelUp"
    command    "heatLevelDown"
    command    "coolLevelUp"
    command    "coolLevelDown"

    attribute  "outdoorHumidity"   , "number"
    attribute  "outdoorTemperature", "number"
    attribute  "lastUpdate"        , "string"
  }

  tiles {
    valueTile("temperature", "device.temperature", width: 2, height: 2, canChangeIcon: true) {
      state("temperature", label: '${currentValue}°', 
        icon: "http://cdn.device-icons.smartthings.com/Weather/weather2-icn@2x.png",
        unit:"F", backgroundColors: [            
          [value: -14, color: "#1e9cbb"],
          [value: -10, color: "#90d2a7"],
          [value: -5 , color: "#44b621"],
          [value: -2 , color: "#f1d801"],
          [value:  0 , color: "#153591"],
          [value:  7 , color: "#1e9cbb"],
          [value:  15, color: "#90d2a7"],           
          [value:  23, color: "#44b621"],
          [value:  29, color: "#f1d801"],
          [value:  31, color: "#153591"],
          [value:  44, color: "#1e9cbb"],
          [value:  59, color: "#90d2a7"],
          [value:  74, color: "#44b621"],
          [value:  84, color: "#f1d801"],
          [value:  95, color: "#d04e00"],
          [value:  96, color: "#bc2323"]
        ]
      )
    }

    standardTile("thermostatMode", "device.thermostatMode", inactiveLabel: false, canChangeIcon: true) {
      state "off" , label:'${name}', action:"thermostat.cool", icon: "st.Outdoor.outdoor19"
      state "cool", label:'${name}', action:"thermostat.heat", icon: "st.Weather.weather7" , backgroundColor: '#1e9cbb'
      state "heat", label:'${name}', action:"thermostat.auto", icon: "st.Weather.weather14", backgroundColor: '#E14902'  
      state "auto", label:'${name}', action:"thermostat.off" , icon: "st.Weather.weather3" , backgroundColor: '#44b621'
    }
    
    standardTile("thermostatFanMode", "device.thermostatFanMode", inactiveLabel: false, canChangeIcon: true) {
      state "auto"     , label:'${name}', action:"thermostat.fanAuto"     , icon: "st.Appliances.appliances11", backgroundColor: '#44b621'
      state "circulate", label:'${name}', action:"thermostat.fanCirculate", icon: "st.Appliances.appliances11", backgroundColor: '#44b621'
      state "on"       , label:'${name}', action:"thermostat.fanOn"       , icon: "st.Appliances.appliances11", backgroundColor: '#44b621'
    }

    controlTile("coolSliderControl", "device.coolingSetpoint", "slider", height: 3, width: 1, inactiveLabel: false) {
      state "setCoolingSetpoint", label:'Set temperarure to', action:"thermostat.setCoolingSetpoint", 
      backgroundColors:[
        [value: 0 , color: "#153591"],
        [value: 7 , color: "#1e9cbb"],
        [value: 15, color: "#90d2a7"],           
        [value: 23, color: "#44b621"],
        [value: 29, color: "#f1d801"],
        [value: 31, color: "#153591"],
        [value: 44, color: "#1e9cbb"],
        [value: 59, color: "#90d2a7"],
        [value: 74, color: "#44b621"],
        [value: 84, color: "#f1d801"],
        [value: 95, color: "#d04e00"],
        [value: 96, color: "#bc2323"]
      ]               
    }
    
    valueTile("coolingSetpoint", "device.coolingSetpoint", inactiveLabel: false) {
      state "default", label:'Cool\n${currentValue}°', unit:"F",
      backgroundColors: [
        [value: 0 , color: "#153591"],
        [value: 7 , color: "#1e9cbb"],
        [value: 15, color: "#90d2a7"],           
        [value: 23, color: "#44b621"],
        [value: 29, color: "#f1d801"],
        [value: 31, color: "#153591"],
        [value: 44, color: "#1e9cbb"],
        [value: 59, color: "#90d2a7"],
        [value: 74, color: "#44b621"],
        [value: 84, color: "#f1d801"],
        [value: 95, color: "#d04e00"],
        [value: 96, color: "#bc2323"]
      ]   
    }
    
    valueTile("heatingSetpoint", "device.heatingSetpoint", inactiveLabel: false) {
      state "default", label:'Heat\n${currentValue}°', unit: "F",
      backgroundColors:[
        [value: 0 , color: "#153591"],
        [value: 7 , color: "#1e9cbb"],
        [value: 15, color: "#90d2a7"],           
        [value: 23, color: "#44b621"],
        [value: 29, color: "#f1d801"],
        [value: 31, color: "#153591"],
        [value: 44, color: "#1e9cbb"],
        [value: 59, color: "#90d2a7"],
        [value: 74, color: "#44b621"],
        [value: 84, color: "#f1d801"],
        [value: 95, color: "#d04e00"],
        [value: 96, color: "#bc2323"]
      ]   
    }

    //tile added for operating state - Create the tiles for each possible state, look at other examples if you wish to change the icons here. 
    valueTile("thermostatOperatingState", "device.thermostatOperatingState", inactiveLabel: false) {
      state "Heating", label:'${name}', backgroundColor : '#E14902', icon: "st.Weather.weather14"
      state "Cooling", label:'${name}', backgroundColor : '#1e9cbb', icon: "st.Weather.weather7"
      state "Idle"   , label:'${name}', icon: ""
      state "Unknown", label:'${name}', backgroundColor : '#cc0000', icon: ""
    }

    standardTile("fanOperatingState", "device.fanOperatingState", inactiveLabel: false) {
      state "On"     , label:'${name}',icon: "st.Appliances.appliances11", backgroundColor : '#53a7c0'
      state "Idle"   , label:'${name}',icon: "st.Appliances.appliances11"
      state "Unknown", label:'${name}',icon: "st.Appliances.appliances11", backgroundColor : '#cc0000'
    }

    standardTile("refresh", "device.thermostatMode", inactiveLabel: false, decoration: "flat") {
      state "default", action:"polling.poll", icon:"st.secondary.refresh"
    }

    standardTile("heatLevelUp", "device.heatingSetpoint", canChangeIcon: false, inactiveLabel: false) {
      state "heatLevelUp", label:'  ', action:"heatLevelUp", icon:"st.thermostat.thermostat-up"
    }
    standardTile("heatLevelDown", "device.heatingSetpoint", canChangeIcon: false, inactiveLabel: false) {
      state "heatLevelDown", label:'  ', action:"heatLevelDown", icon:"st.thermostat.thermostat-down"
    }
    standardTile("coolLevelUp", "device.heatingSetpoint", canChangeIcon: false, inactiveLabel: false) {
      state "coolLevelUp", label:'  ', action:"coolLevelUp", icon:"st.thermostat.thermostat-up"
    }
    standardTile("coolLevelDown", "device.heatingSetpoint", canChangeIcon: false, inactiveLabel: false) {
      state "coolLevelDown", label:'  ', action:"coolLevelDown", icon:"st.thermostat.thermostat-down"
    }

    valueTile("relativeHumidity", "device.relativeHumidity", inactiveLabel: false)
    {
      state "default", label:'Humidity\n${currentValue}%',
      icon: "http://cdn.device-icons.smartthings.com/Weather/weather12-icn@2x.png",
      unit:"%", backgroundColors : [
        [value: 01, color: "#724529"],
        [value: 11, color: "#724529"],
        [value: 21, color: "#724529"],
        [value: 35, color: "#44b621"],
        [value: 49, color: "#44b621"],
        [value: 50, color: "#1e9cbb"]
      ]
    }


    valueTile("outdoorTemperature", "device.outdoorTemperature", width: 1, height: 1, canChangeIcon: true) {
      state("temperature", label: 'Outdoor\n ${currentValue}°',
      icon: "http://cdn.device-icons.smartthings.com/Weather/weather2-icn@2x.png",
      unit:"F", backgroundColors: [
        [value: -31, color: "#003591"],
        [value: -10, color: "#90d2a7"],
        [value: -5, color: "#44b621"],
        [value: -2, color: "#f1d801"],
        [value: 0, color: "#153591"],
        [value: 7, color: "#1e9cbb"],
        [value: 00, color: "#cccccc"],
        [value: 31, color: "#153500"],
        [value: 44, color: "#1e9cbb"],
        [value: 59, color: "#90d2a7"],
        [value: 74, color: "#44b621"],
        [value: 84, color: "#f1d801"],
        [value: 95, color: "#d04e00"],
        [value: 96, color: "#bc2323"]
      ]
      )
    }

    valueTile("outdoorHumidity", "device.outdoorHumidity", inactiveLabel: false){
      state "default", label:'Outdoor\n ${currentValue}%', 
      icon: "http://cdn.device-icons.smartthings.com/Weather/weather12-icn@2x.png",
      unit:"%", backgroundColors : [
        [value: 01, color: "#724529"],
        [value: 11, color: "#724529"],
        [value: 21, color: "#724529"],
        [value: 35, color: "#44b621"],
        [value: 49, color: "#44b621"],
        [value: 70, color: "#449c00"],
        [value: 90, color: "#009cbb"]
      ]
    }

    valueTile("status", "device.lastUpdate", width: 3, height: 1, decoration: "flat") {
      state "default", label: 'Last Update: ${currentValue}'
    }

    main "temperature"
    
    details(["temperature", 
             "thermostatMode", 
             "thermostatFanMode",   
             "heatLevelUp", 
             "heatingSetpoint" , 
             "heatLevelDown", 
             "coolLevelUp",
             "coolingSetpoint", 
             "coolLevelDown" ,
             "thermostatOperatingState",
             "fanOperatingState",
             "refresh",
             "relativeHumidity",
             "outdoorTemperature",
             "outdoorHumidity", 
             "status"
    ])
             
  }
  
  simulator {
  }
  
}


// This device does not use the parse interface
def parse(String description) {
}

//
// handle commands
//

def heatLevelUp()
{    
  if (settings.tempScale == "F")
    setHeatingSetpoint( device.currentValue("heatingSetpoint") + 1.0 )
  else
    setHeatingSetpoint( device.currentValue("heatingSetpoint") + 0.5 )    
}

def heatLevelDown()
{
  if (settings.tempScale == "F")
    setHeatingSetpoint( device.currentValue("heatingSetpoint") - 1.0 )
  else
    setHeatingSetpoint( device.currentValue("heatingSetpoint") - 0.5 )    
}


def coolLevelUp() {
  if (settings.tempScale == "F")
    setHeatingSetpoint( device.currentValue("coolingSetpoint") + 1.0 )
  else
    setHeatingSetpoint( device.currentValue("coolingSetpoint") + 0.5 )  
}

def coolLevelDown()
{
  if (settings.tempScale == "F")
    setHeatingSetpoint( device.currentValue("coolingSetpoint") - 1.0 )
  else
    setHeatingSetpoint( device.currentValue("coolingSetpoint") - 0.5 ) 
}

def setHeatingSetpoint(Double temp)
{
  log.debug "setHeatingSetpoint( ${temp} )"
  
  // Limit Check
  if (settings.tempScale == "F") {

    if( temp > 95) temp = 95
    if( temp < 50) temp = 50

  } else {

    if( temp > 37) temp = 37
    if( temp < 10) temp = 10

  }
  
  // Send the command to TCC
  data.SystemSwitch = 'null' 
  data.HeatSetpoint = temp
  data.CoolSetpoint = 'null'
  data.HeatNextPeriod = 'null'
  data.CoolNextPeriod = 'null'
  data.StatusHeat='1'
  data.StatusCool='1'
  data.FanMode = 'null'
  setStatus()

  // Check if the command was sent successfully
  if(data.SetStatus==1)
    sendEvent(name: 'heatingSetpoint', value: temp as double)
}

def setCoolingSetpoint(double temp) {

  log.debug "setCoolingSetpoint( ${temp} )"
  
  // Limit Check
  if (settings.tempScale == "F") {

    if( temp > 95) temp = 95
    if( temp < 50) temp = 50

  } else {

    if( temp > 37) temp = 37
    if( temp < 10) temp = 10

  }
  
  // Send the command to TCC
  data.SystemSwitch   = 'null' 
  data.HeatSetpoint   = 'null'
  data.CoolSetpoint   = temp
  data.HeatNextPeriod = 'null'
  data.CoolNextPeriod = 'null'
  data.StatusHeat     = 1
  data.StatusCool     = 1
  data.FanMode        = 'null'
  setStatus()

  // Check if the command was sent successfully
  if(data.SetStatus == 1)
    sendEvent(name: 'coolingSetpoint', value: temp as double)
}

def setTargetTemp(double temp) {

  log.debug "setTargetTemp( ${temp} )"
  
  data.SystemSwitch   = 'null' 
  data.HeatSetpoint   = temp
  data.CoolSetpoint   = temp
  data.HeatNextPeriod = 'null'
  data.CoolNextPeriod = 'null'
  data.StatusHeat     = 1
  data.StatusCool     = 1
  data.FanMode        = 'null'
  setStatus()
}

def off() {
  setThermostatMode(2)
}

def auto() {
  setThermostatMode(4)
}

def heat() {
  setThermostatMode(1)
}

def emergencyHeat() {

}

def cool() {
  setThermostatMode(3)
}

def setThermostatMode(mode) {

  log.debug "setThermostatMode( ${mode} )"
  
  data.SystemSwitch   = mode 
  data.HeatSetpoint   = 'null'
  data.CoolSetpoint   = 'null'
  data.HeatNextPeriod = 'null'
  data.CoolNextPeriod = 'null'
  data.StatusHeat     = 1
  data.StatusCool     = 1 
  data.FanMode        = 'null'
	setStatus()
  
  def switchPos
  if(mode==1)
  	switchPos = 'heat'
  if(mode==2)
  	switchPos = 'off'
  if(mode==3)
  	switchPos = 'cool'
  if(mode==4 || swithPos == 5)
  	switchPos = 'auto'

  if(data.SetStatus==1)
    sendEvent(name: 'thermostatMode', value: switchPos)
  
}

def fanOn() {
  setThermostatFanMode(1)
}

def fanAuto() {
  setThermostatFanMode(0)
}

def fanCirculate() {
  setThermostatFanMode(2)
}

def setThermostatFanMode(mode) {  
  
  log.debug "setThermostatFanMode( ${mode} )"
    
  data.SystemSwitch   = 'null' 
  data.HeatSetpoint   = 'null'
  data.CoolSetpoint   = 'null'
  data.HeatNextPeriod = 'null'
  data.CoolNextPeriod = 'null'
  data.StatusHeat     = 'null'
  data.StatusCool     = 'null'
  data.FanMode        = mode

  setStatus()

  def fanMode

  if(mode==0)
  	fanMode = 'auto'
  	
  if(mode==1)
  	fanMode = 'on'
  	
  if(mode==2)
  	fanMode = 'circulate'

  if(data.SetStatus==1)
    sendEvent(name: 'thermostatFanMode', value: fanMode)    
}


def poll() {
	refresh()
}


def login() {  
  log.debug "login()"

  def params = [
    uri: 'https://www.mytotalconnectcomfort.com/portal',
    headers: [
      'Content-Type': 'application/x-www-form-urlencoded',
      'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8',
      'Accept-Encoding': 'sdch',
      'Host': 'www.mytotalconnectcomfort.com',
      'DNT': '1',
      'Origin': 'www.mytotalconnectcomfort.com/portal/',
      'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36'
    ],
    body: [timeOffset: '240', UserName: "${settings.username}", Password: "${settings.password}", RememberMe: 'false']
  ]

  data.cookiess = ''

  log.debug "Request: $params"
  httpPost(params) { response ->
    log.debug "Response: $response.data"
    
    response.getHeaders('Set-Cookie').each {
      String cookie = it.value.split(';|,')[0]
      log.debug "Adding cookie to jar: $cookie"
      
      if(cookie != ".ASPXAUTH_TH_A=") 
        data.cookiess = data.cookiess+cookie+';'
      
    }
    log.debug "Cookie Jar: $data.cookiess"
  }
}

def getStatus() {
  log.debug "getStatus()"

  def params = [
    uri: "https://www.mytotalconnectcomfort.com/portal/Device/CheckDataSession/${settings.honeywelldevice}",
    headers: [
      'Accept': '*/*',
      'DNT': '1',
      'Cache' : 'false',
      'dataType': 'json',
      'Accept-Encoding': 'plain',
      'Cache-Control': 'max-age=0',
      'Accept-Language': 'en-US,en,q=0.8',
      'Connection': 'keep-alive',
      'Referer': 'https://www.mytotalconnectcomfort.com/portal',
      'X-Requested-With': 'XMLHttpRequest',
      'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36',
      'Cookie': data.cookiess        
    ],
  ]

  log.debug "Request: $params"
  httpGet(params) { response ->
    log.debug "Response: $response.data"
    
    // Pull relivant data out of the response
    def curTemp = response.data.latestData.uiData.DispTemperature
    def fanMode = response.data.latestData.fanData.fanMode
    def switchPos = response.data.latestData.uiData.SystemSwitchPosition
    def coolSetPoint = response.data.latestData.uiData.CoolSetpoint
    def heatSetPoint = response.data.latestData.uiData.HeatSetpoint
    def statusCool = response.data.latestData.uiData.StatusCool
    def statusHeat = response.data.latestData.uiData.StatusHeat
    def curHumidity = response.data.latestData.uiData.IndoorHumidity
    def Boolean hasOutdoorHumid = response.data.latestData.uiData.OutdoorHumidityAvailable
    def Boolean hasOutdoorTemp = response.data.latestData.uiData.OutdoorTemperatureAvailable
    def curOutdoorHumidity = response.data.latestData.uiData.OutdoorHumidity
    def curOutdoorTemp = response.data.latestData.uiData.OutdoorTemperature
    def displayUnits = response.data.latestData.uiData.DisplayUnits
    def fanIsRunning = response.data.latestData.fanData.fanIsRunning
    def equipmentStatus = response.data.latestData.uiData.EquipmentOutputStatus

    // Reformat data
    state.DisplayUnits = $displayUnits

    def operatingState = "Unknown"
    if (equipmentStatus == 0) 
      operatingState = "Idle"
    if (equipmentStatus == 1) 
      operatingState = "Heating"
    if (equipmentStatus == 2) 
      operatingState = "Cooling"       

    def fanState = "Unknown"
    if (fanIsRunning)
      fanState = "On"
    else 
      fanState = "Idle"       

    if(fanMode == 0)
      fanMode = 'auto'
    if(fanMode == 1)
      fanMode = 'on'
    if(fanMode == 2)
      fanMode = 'circulate'

    if(switchPos == 1)
      switchPos = 'heat'
    if(switchPos == 2)
      switchPos = 'off'
    if(switchPos == 3)
      switchPos = 'cool'
    if(switchPos == 4 || switchPos==5)
      switchPos = 'auto'

    def formattedCoolSetPoint = String.format("%5.1f", coolSetPoint)
    def formattedHeatSetPoint = String.format("%5.1f", heatSetPoint)
    def formattedTemp = String.format("%5.1f", curTemp)

    def finalCoolSetPoint = formattedCoolSetPoint as BigDecimal
    def finalHeatSetPoint = formattedHeatSetPoint as BigDecimal
    def finalTemp = formattedTemp as BigDecimal

    //Send events 
    sendEvent(name: 'thermostatOperatingState', value: operatingState)
    sendEvent(name: 'fanOperatingState'       , value: fanState)
    sendEvent(name: 'thermostatFanMode'       , value: fanMode)
    sendEvent(name: 'thermostatMode'          , value: switchPos)
    sendEvent(name: 'coolingSetpoint'         , value: finalCoolSetPoint )
    sendEvent(name: 'heatingSetpoint'         , value: finalHeatSetPoint )
    sendEvent(name: 'temperature'             , value: finalTemp, state: switchPos)
    sendEvent(name: 'relativeHumidity'        , value: curHumidity as Integer)

    // Display last update time
    if (settings.tzOffset == null)
      settings.tzOffset = -5

    def now = new Date()
    def tf = new java.text.SimpleDateFormat("MM/dd/yyyy h:mm a")
    tf.setTimeZone(TimeZone.getTimeZone("GMT${settings.tzOffset}"))
    def newtime = "${tf.format(now)}" as String   
    sendEvent(name: "lastUpdate", value: newtime, descriptionText: "Last Update: $newtime")


    if (enableOutdoorTemps == "Yes")
    {
      if (hasOutdoorHumid)
        sendEvent(name: 'outdoorHumidity', value: curOutdoorHumidity as Integer)

      if (hasOutdoorTemp)
        sendEvent(name: 'outdoorTemperature', value: curOutdoorTemp as Integer)
    }
  }
}

def setStatus() {

  log.debug "setStatus()"
  data.SetStatus = 0

  // Log in and get a new session cookie
  login()

  def params = [
    uri: "https://www.mytotalconnectcomfort.com/portal/Device/SubmitControlScreenChanges",
    headers: [
      'Accept': 'application/json, text/javascript, */*; q=0.01',
      'DNT': '1',
      'Accept-Encoding': 'gzip,deflate,sdch',
      'Cache-Control': 'max-age=0',
      'Accept-Language': 'en-US,en,q=0.8',
      'Connection': 'keep-alive',
      'Host': 'rs.alarmnet.com',
      'Referer': "https://www.mytotalconnectcomfort.com/portal/Device/Control/${settings.honeywelldevice}",
      'X-Requested-With': 'XMLHttpRequest',
      'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36',
      'Cookie': data.cookiess        
    ],
    body: [
      DeviceID       : "${settings.honeywelldevice}",
      SystemSwitch   : data.SystemSwitch,
      HeatSetpoint   : data.HeatSetpoint, 
      CoolSetpoint   : data.CoolSetpoint, 
      HeatNextPeriod : data.HeatNextPeriod,
      CoolNextPeriod : data.CoolNextPeriod,
      StatusHeat     : data.StatusHeat,
      StatusCool     : data.StatusCool,
      FanMode        : data.FanMode,
      ThermostatUnits: settings.tempScale
    ]
  ]

  log.debug "Request: $params"
  httpPost(params) { response ->
    log.debug "Response: $response"
  }

  data.SetStatus = 1
  log.debug "SetStatus is 1 now"
}


def getHumidifierStatus()
{
  def params = [
    uri: "https://www.mytotalconnectcomfort.com/portal/Device/Menu/GetHumData/${settings.honeywelldevice}",
    headers: [
      'Accept': '*/*',
      'DNT': '1',
      'dataType': 'json',
      'cache': 'false',
      'Accept-Encoding': 'plain',
      'Cache-Control': 'max-age=0',
      'Accept-Language': 'en-US,en,q=0.8',
      'Connection': 'keep-alive',
      'Host': 'rs.alarmnet.com',
      'Referer': 'https://www.mytotalconnectcomfort.com/portal/Menu/${settings.honeywelldevice}',
      'X-Requested-With': 'XMLHttpRequest',
      'User-Agent': 'Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/28.0.1500.95 Safari/537.36',
      'Cookie': data.cookiess        
    ],
  ]
  httpGet(params) { response ->
    log.debug "GetHumidity Request was successful, $response.status"
    log.debug "response = $response.data"
    log.trace("lowerLimit: ${response.data.latestData.humData.lowerLimit}")        
    log.trace("upperLimit: ${response.data.humData.upperLimit}")        
    log.trace("SetPoint: ${response.data.humData.Setpoint}")        
    log.trace("DeviceId: ${response.data.humData.DeviceId}")        
    log.trace("IndoorHumidity: ${response.data.humData.IndoorHumidity}")        
  }
}

def refresh() {
	log.debug "refresh()"
  login()
  getStatus()
}

def isLoggedIn() {
  if(!data.auth) {
    log.debug "No data.auth"
    return false
  }
  
  def now = new Date().getTime();
  return data.auth.expires_in > now
}

def updated()
{
  log.debug "updated()"
}

def installed() {
	log.debug "installed()"
}

def api(method, args = [], success = {}) {
	log.debug "api(...)"
}

def doRequest(uri, args, type, success) {
	log.debug "doRequest(...)"
}

