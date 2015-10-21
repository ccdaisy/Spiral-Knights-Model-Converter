#Spiral Knights Model to OBJ
The new and improved version of "Spiral Spy XML to OBJ"!

The current code in the repository **is** up to date (As compared to my eclipse workspace).

The latest release is stable, **however the code in the repository is not stable** (This means you can still download from Releases and be fine)


#TODAY IS A BIG DAY!
That's right. Spiral Knights Model to OBJ is starting to accept .DAT files! Some time today I will hopefully fix the error that is causing the reading code shown in the repository to be faulty. (If you are curious, the field reader in BinaryImporter.java seems to not read .DAT files correctly. One of the field's length is somewhere near -1,000,000,000 for some reason. I am working this out.)

**BEFORE YOU GET TOO HAPPY, THERE IS SOME DISCOURAGING NEWS**
.DAT reading will have to come in either 2 or 3 updates (3 if I include the AutoDesk FBX exporter). Here's why and how they will be released:


 - V0.8) .DAT to Model with no textures. Reason: The first method of reading .DAT uses the fewest amount of code from the Clyde library. It will be the easiest to create. I am creating an XML directly from the DAT, then converting the XML to OBJ.
 - V0.9 OR 1.0 Full Release) .DAT to Model with textures. This update will probably be more difficult for me to create, as I will have to grab the OpenGL classes (or lwjgl classes?) in Clyde for the texture mapping function, so that I can recreate the texture for the OBJ.

#WHAT CAN IT CONVERT?
 - XML Exported from Spiral Spy (http://spiral.onyxbits.de/download)
 - [Not ready] DAT Directly from the Spiral Knights game files

#WORK SO FAR
 - Improved file reading (Slightly faster reading) + Save/Open dialog
 - Small-scale .DAT reading (Returns the UTF read from the FileInputStream, which isn't the right way to go for conversion.)

#ISSUES THAT HINDER WORK
 - Schoolwork

#PLANS
This is the list of what I plan to improve or add in this project.

*CONFIRMED*
 - [Done!] Project revamp that allows save/open dialog as opposed to the terrible method of text copy/paste 
 - [In progress...] Project revamp that allows directly importing .DAT (And .PNG texture) files and saving as .OBJ + Textures

*PLAUSIBLE*
 - [Currently inactive] Model previewer (So the program MIGHT be like Spiral Spy with an "Export OBJ" option.)
 - [Currently inactive] Export AutoDesk .FBX format for model, texture, and animation. (This would be easier than the .XMDL)

*CONSIDERED BUT NOT GOING TO BE IMPLEMENTED*
 - Compact and user friendly model + animation editor in the program
 - Export VALVE .MDL files for usage in Source Film-Maker

#WHAT IS A SPIRAL KNIGHTS .DAT AND HOW DOES IT WORK?
 The Spiral Knights .DAT model is Three Rings's way of packaging models, animations, and some scripts. It is, in fact, a Java code that was merely exported as a custom Object via a FileOutputStream!
