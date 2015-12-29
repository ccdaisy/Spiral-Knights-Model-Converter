#Spiral Knights Model to OBJ

The new and improved version of "Spiral Spy XML to OBJ"!

The current code in the repository **is not** up to date (As compared to my eclipse workspace).

The latest release is stable, and the code in the repository is stable.

#WHAT CAN IT CONVERT?
 - XML Exported from Spiral Spy (http://spiral.onyxbits.de/download) (Fun fact: Spiral Spy uses the same exact code I use)
 - [Works!] DAT Directly from the Spiral Knights game files (NO TEXTURES)

#PLANS
This is the list of what I plan to improve or add in this project.

*CONFIRMED*
 - [Done!] Project revamp that allows save/open dialog as opposed to the terrible method of text copy/paste 
 - [In progress... (50%)] Project revamp that allows directly importing .DAT (And .PNG texture) files and saving as .OBJ + Textures

*PLAUSIBLE*
 - [Currently inactive] Export Blender .BLEND format for model, texture, and animation.

*CONSIDERED BUT NOT GOING TO BE IMPLEMENTED*
 - Compact and user friendly model + animation editor in the program
 - Export VALVE .MDL files for usage in Source Film-Maker

#WORK SO FAR
 - Improved file reading (Slightly faster reading) + Save/Open dialog
 - Full-scale .DAT reading. It can now convert the model. Not the texture, however.

#WHAT IS A SPIRAL KNIGHTS .DAT AND HOW DOES IT WORK?
 The Spiral Knights .DAT model is Three Rings's way of packaging models, animations, and some scripts. It is, in fact, a Java code that was merely exported as a custom Object via a FileOutputStream! A new discovery has led me to find out that I am actually missing code since the .DAT indexes code from my libraries. I will also have to restore the location of the DAT handling code in my repository/workspace so that the .DAT can properly index the code.
