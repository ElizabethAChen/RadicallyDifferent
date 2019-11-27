# RadicallyDifferent
Approaching reading simplified Chinese characters through radical education
This is an Android Studio Kotlin app meant to increase education around Simplified Chinese characters through education about radicals.

Please see the Instructions below for reccommedations on how to install. 

Installation Instructions
Prerequisites:
Minimum: Linux 18.04 with Android Studio with virtualization enabled in the BIOS
Optional: Galaxy S8, appropriate USB charging cable
Testing on a Virtual Device
Given that the user has an up-to-date copy of Android Studio on Linux and the user has gone into their BIOS to activate virtualization settings.
Download the .zip folder of all files. Save this to a place that will be remembered. Unzip the folder.
Open Android Studios. From the Android Studios menu select File > New > Import Project. Select the unzipped folder. Click [OK].
Wait for the project to build. It will be complete when the Build window at the bottom of the screen shows all check marks it will also say “Build: completed successfully at {date time}” .

From the Android Studios menu select Tools > AVD Manager. 
Press the [+ Create Virtual Device…] button
Select Category: Phone
For some of these options it may be required to download the feature before use. Do so, then move onto the next step. 
Select Nexus 5 then [Next]
Select Pie then [Next]
Select [Finish]. The device is now able to be selected. Close the AVD Manager window.
Press the green arrow button near the top right of the screen to start the app. A virtual phone window will appear on the screen momentarily. 

Testing on a Physical Device
Given that the user has an up-to-date copy of Android Studio on Linux, a Galaxy S8 and its appropriate USB charging cable
Next download the .zip folder of all files. Save this to a place that will be remembered. Unzip the folder.
If required, enable developer mode on the Galaxy S8 smartphone by opening Settings > About Phone. Tap the Build Number section 7 times. A message will display indicating that “Developer mode has been turned on”. This may be turned off later.
Open the Linux Command Terminal
Copy the following code and paste it into the command terminal via right mouse click then press return.
 sudo usermod -aG plugdev $LOGNAME
sudo apt-get install android-tools-adb 
Save all work, close all open applications and restart. 
Plug the appropriate charging cable into both the USB slot and the Galaxy phone
If the phone requests access to your data, press [Allow]
Open the Linux Command Terminal
Copy the following code and paste it into the command terminal
adb start-server
The terminal should say “daemon started successfully” 
adb devices
The terminal should indicate at least one connected device. If this step fails, restart from Step 6.
Open Android Studios
From the Android Studios menu select File > New > Import Project. Select the unzipped folder. Click [OK].
Wait for the project to build. It will be complete when the Build window at the bottom of the screen shows all check marks it will also say “Build: completed successfully at {date time}” .
Press the AVD Manager button next to the green play button. Select the Galaxy S8. 
Press the green arrow button near the top right of the screen to start the app.

 Radically Different will download like a normal app to use the app on the Galaxy S8 smartphone. Once the app appears on the phone, it may be disconnected from the charging cable.
