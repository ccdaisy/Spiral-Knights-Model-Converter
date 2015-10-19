# Spiral Knights Model to OBJ
The new and improved version of "Spiral Spy XML to OBJ"!



#WORK SO FAR
 - Improved file reading (Slightly faster reading) + Save/Open dialog (ONLY WORKS WITH XML RIGHT NOW. TUTORIAL IN RELEASES!)
 - Preparations for .DAT reading

#ISSUES THAT HINDER WORK
 - Schoolwork
 - .DAT Model format understanding
 - Work ethic

#PLANS
This is the list of what I plan to improve or add in this project.

*CONFIRMED*
 - [Done!] Project revamp that allows save/open dialog as opposed to the terrible method of text copy/paste 
 - [In progress...] Project revamp that allows directly importing .DAT (And .PNG texture) files and saving as .OBJ + Textures

*PLAUSIBLE*
 - [Currently inactive] Export AutoDesk .FBX format for model, texture, and animation. (This would be easier than the .XMDL)

*CONSIDERED BUT NOT GOING TO BE IMPLEMENTED*
 - Compact and user friendly model + animation editor in the program
 - Export VALVE .MDL files for usage in Source Film-Maker

#WHAT IS A SPIRAL KNIGHTS .DAT AND HOW DOES IT WORK?
 The Spiral Knights .DAT model is Three Rings's way of packaging models, animations, and some scripts. It is, in fact, a Java code that was merely exported with a FileOutputStream! There is a catch, however - The way their code reads it (Clyde is the library I am focusing on, since it handles imports and exports) - It is a very large code, and is really stumping me in the aspect of how it applies the .DAT to their code libraries. I have been working on this project on and off since late July.
