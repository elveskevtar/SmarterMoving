SmarterMoving Mod
=================
This is Elveskevtar's reboot of Divisor and JonnyNova's original SmartMoving
mod. The plan is to restore the mod to a very similar state of the original
when it was being actively maintained. However, not all details will remain
the same. Any and all changes will be subject to potential change from the
original mod. Brand-new aspects not available in the original mod may be
implemented within this reboot.

Licensing Information
---------------------
The SmarterMoving mod reboot is operating under the MIT open source license.
You can find the license in [LICENSE-SmarterMoving.txt](LICENSE-SmarterMoving.txt).

Licenses for MinecraftForge and Paulscode can also be found within this repository.

Contributing
------------
We plan on fostering a collaborative and inclusive community for contributing.
We have an active discord server for those interested in becoming more involved.
See the [Contributing Document](CONTRIBUTING.md) for more information on how you
can make contributions and become involved in the modding community.

Code of Conduct
---------------
In order to maintain a collaborative and inclusive community, we have ground rules
to keep discussion civil and respectful. We also hope to minimize harmful conflict
and harmful communication. While contributing or interacting with our community,
please follow the rules and guidelines within the [Code of Conduct](CODE_OF_CONDUCT.md).

MinecraftForge
==============

Source installation information for modders
-------------------------------------------
This code follows the Minecraft Forge installation methodology. It will apply
some small patches to the vanilla MCP source code, giving you and it access 
to some of the data and functions you need to build a successful mod.

Note also that the patches are built against "unrenamed" MCP source code (aka
srgnames) - this means that you will not be able to read them directly against
normal code.

Source pack installation information:

Standalone source installation
------------------------------

See the Forge Documentation online for more detailed instructions:
http://mcforge.readthedocs.io/en/latest/gettingstarted/

Step 1: Open your command-line and browse to the folder where you extracted the zip file.

Step 2: You're left with a choice.
If you prefer to use Eclipse:
1. Run the following command: "gradlew genEclipseRuns" (./gradlew genEclipseRuns if you are on Mac/Linux)
2. Open Eclipse, Import > Existing Gradle Project > Select Folder 
   or run "gradlew eclipse" to generate the project.
(Current Issue)
4. Open Project > Run/Debug Settings > Edit runClient and runServer > Environment
5. Edit MOD_CLASSES to show [modid]%%[Path]; 2 times rather then the generated 4.

If you prefer to use IntelliJ:
1. Open IDEA, and import project.
2. Select your build.gradle file and have it import.
3. Run the following command: "gradlew genIntellijRuns" (./gradlew genIntellijRuns if you are on Mac/Linux)
4. Refresh the Gradle Project in IDEA if required.

If at any point you are missing libraries in your IDE, or you've run into problems you can run "gradlew --refresh-dependencies" to refresh the local cache. "gradlew clean" to reset everything {this does not affect your code} and then start the processs again.

Should it still not work, 
Refer to #ForgeGradle on EsperNet for more information about the gradle environment.
or the Forge Project Discord discord.gg/UvedJ9m

Forge source installation
-------------------------
MinecraftForge ships with this code and installs it as part of the forge
installation process, no further action is required on your part.

LexManos' Install Video
-----------------------
https://www.youtube.com/watch?v=8VEdtQLuLO0&feature=youtu.be

For more details update more often refer to the Forge Forums:
http://www.minecraftforge.net/forum/index.php/topic,14048.0.html
