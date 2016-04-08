# Total Comfort Control Device Handler

This Device Handler can be used to connect Honeywell Wifi Thermostats to a SmartThings Hub.

##Usage:

1) Log into https://ide.smartthings.com/, and under My Device Types, create a New SmartDevice with the code from thermostat.groovy. Publish the device for yourself.

2) Under My Devices, create a New Device with:

- Name and Label: Display name for thermostat in SmartThings
- Type: Total Comfort API
- Zigbee Id: (blank)
- Device Network Id: Any number (e.g. 99999999)
- Version: Published
- Location: Select your location
- Hub: Select your SmartThings hub
- Group: (blank)

3) Log into the Honeywell Total Comfort web site https://mytotalconnectcomfort.com/portal and click "View Thermostat". You should be at a URL https://mytotalconnectcomfort.com/portal/Device/Control/XXXXXX. Note the numbers at the end (represented by X's).

4) Open SmartThings on your iOS device and configure the new device with your Honeywell username/password, and the numbers at the end of the URL from step 3.
