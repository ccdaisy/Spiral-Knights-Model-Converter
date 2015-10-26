#Spiral Knights Model to OBJ

The new and improved version of "Spiral Spy XML to OBJ"!

The current code in the repository **is not** up to date (As compared to my eclipse workspace).

The latest release is stable, **however the code in the repository is not stable** (This means you can still download from Releases and be fine)


#BIG NEWS!
That's right. Spiral Knights Model to OBJ is starting to accept .DAT files! Hopefully some time within the next 7 days I will fix the error that is causing the reading code shown in the repository to be faulty. NEW DISCOVERY: I HAVE to use the code in clyde, so textures should be out relatively quickly.

#WHAT CAN IT CONVERT?
 - XML Exported from Spiral Spy (http://spiral.onyxbits.de/download) (Fun fact: Spiral Spy uses the same exact code I use)
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
 The Spiral Knights .DAT model is Three Rings's way of packaging models, animations, and some scripts. It is, in fact, a Java code that was merely exported as a custom Object via a FileOutputStream! A new discovery has led me to find out that I am actually missing code since the .DAT indexes code from my libraries. I will also have to restore the location of the DAT handling code in my repository/workspace so that the .DAT can properly index the code.
