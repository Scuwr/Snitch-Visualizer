## Snitch-Visualizer (FML 1.8)
A mod to render snitches in civcraft

Compiling from Source
---

This mod is compiled using the Forge Mod Loader (FML) mod pack which includes data from the Minecraft Coder Pack (MCP).

To compile this mod from the source code provided

1. Install the [1.8-Recommended](http://adfoc.us/serve/sitelinks/?id=271228&url=http://files.minecraftforge.net/maven/net/minecraftforge/forge/1.8-11.14.1.1334/forge-1.8-11.14.1.1334-src.zip) build from MinecraftForge.net or compile using [another dependency version](http://files.minecraftforge.net/) (OTHER VERSIONS OF FORGE ARE CURRENTLY NOT SUPPORTED!)
2. Follow the [Wuppy's Minecraft Forge Modding Tutorial](http://www.wuppy29.com/minecraft/1-8-tutorial/forge-modding-tutorial-1-8-set-up-part-2-forge-setup/) and set up the FML pack.
3. Remove the src folder provided by FML and replace it with the src folder from this repository
4. Finally, ensure your Java IDE is running under at least Java 1.7 and compile

Happy Hacking!

---
In the event Wuppy's Minecraft Forge Modding is unavailable:

The first thing you have to do is download Forge from the website. You can get the right version of Forge over here. The file you want to download is the src version of Minecraft forge for 1.8. You can either go for the recommended version, but that is mostly the recommended one for mod users. I suggest downloading the newest version of Minecraftforge.

Once you have downloaded this file you should create a folder called something like ForgeMods, because you will keep all of your own code and Forge in this folder. When you have made this folder open up the zip file and paste all of the files in there. Once that is done open up a commandprompt by shift right clicking in that folder. As an Eclipse user, run the following command.

    gradlew setupDecompWorkspace eclipse

    gradlew setupDecompWorkspace eclipse

If you are an IntelliJ IDEA user you have to run this command.

    gradlew setupDecompWorkspace idea

    gradlew setupDecompWorkspace idea

If, when you run this, you get an error mentioning you have to set the JAVA_HOME variable, go back to the user variables as shown in Part 1 and create a variable called JAVA_HOME. This variable has to be very similar to the PATH variable, but it shouldn’t contain the /bin part. For some reason you only need the JAVA_HOME on some pc’s.

IntelliJ is a similar program to Eclipse. If you are advanced enough with Java or if you have used IntelliJ for a while and prefer it you can go with that, but if you don’t know which one to choose and this is your first time programming with Java you should stick with Eclipse, because that is what I will be using in the tutorials.

Either one of these commands will download everything you need to start coding with Forge.

For Mac be sure to use ./gradlew in the place of gradlew.

Once you have ran all of these commands you have to open up Eclipse and point the workspace, which pops up during startup, to the eclipse folder in the ForgeMods folder you created before running the command.

If you want to launch Minecraft in your development environment you have to click on the little arrow next to the green launch button at the top. Then select Client or Server and Minecraft should start for you.

To get to the Minecraft source code you can open the forge library which should be at the bottom of the library list. You can’t edit any of the code in there, but you can look through it to learn why and how something works in Minecraft.

You will also find a file called ExampleMod in the workspace. The code in this file will be explained in the Basic File tutorial.

- See more at: http://www.wuppy29.com/minecraft/1-8-tutorial/forge-modding-tutorial-1-8-set-up-part-2-forge-setup/#sthash.cLznDrYe.dpuf
