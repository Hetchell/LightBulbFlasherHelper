# Hello, this is my first. For Light Bulb Flasher #066(and others if possible)

### Introduction:

This Forge mod is used to recreate the helper codebase that makes Light Bulb Flasher #066 possible.
I could not directly use the code shown in the video because of legality. This forge mod
will not be good, I am only a beginner. 

### Inspiration/Helpers:

The idea to use Firmata4j is from the Dream Team. 
The base material was figured out from watching Forge modding tutorials. 
The ConfigHelper was figured out by studying SuperCoder7979's code.
<p>
The config file(lightbulbflasher.json) is contained in run/config folder. 
</p>

### Things to get out of the way:

WARNING: You have to inject the needed jars(just look at gradle external library - gradle libraries for minecraft)
into the generated jar in the build/lib folder, in order to be able to package the mod to work in direct Forge Minecraft(not 
this, which is considered dev environment). If you don't know how to, just use this. If Gradle give you issue, you can try:
<br>NOTE: I USE INTELLIJ. THE TIPS BELOW IS FOR INTELLJ.
<p>1. Check if the gradle settings show that it builds on jdk-1.8.0_202(I use this)
<br>2. Check the project structure and make sure project sdk is 1.8.0_202. 
<br>3. Make sure your gradle.build, gradle.properties and gradle-wrapper.properties
is correct. Best is just clone repository using the VCS feature. 
<p>If it still don't work, do internet searching or ask GPT-4. 

#### NOTES[Please Read]:

1. The hardware portion is simple, so no circuit diagram is given. Take the defpin
you define, connect it to a resistor to a NPN transistor(I suggest using BJT). The collector
goes to the bulb, the other end goes to the 5V from the board. The emitter of the transistor 
goes to ground. 
2. Before you use, you must load StandardFirmata into the board. To do so, you need to 
download Arduino IDE, then go to file/examples/firmata/standardfirmata. Upload the sketch into the board.
If error occurs, it is possible that you USB -> UART is a CH340. Then you need to search for
drivers and download into your computer(search 'CH340' on the internet, it should 
come out easy).
3. DO NOT MODIFY THE CONFIGDATA CLASS, unless you want to mess up the execution of code.
If you want to change configuration, go and edit the lightbulbflasher.json file, and only
the parts after the "abcde.." : part. i.e. "checking_radius" : 10 modify to 
"checking_radius" : 17. 
4. When you modify lightbulbflasher.json, do it with sensibleness. boolean is true/false,
integers data values have to be more than 0. default_pin has to exist on your
Arduino board's digital pin set(this becomes defpin). For what can be written in entityList_affected, see
the dejt.kaku.flasher.mobbehaviour/MobBehaviour:getMobList(V) method's switch cases. 
5. If you mess up the lightbulbflasher.json, do not panic. Delete the file(or rename it to something else),
then rerun the runClient task. It should regenerate with the default shown in ConfigData.
