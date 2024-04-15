#Steps to Run The Project in Android Studio:-
1.Android Version using: 
Android Studio Iguana | 2023.2.1 Patch 1
2.Download the Code Project and run by extracting source code from zip.
OR Get it from VCS Plugin inside Android studio using:- https://github.com/ajayhack/DemoImageCaching.git
3.Build and Sync Full project and run on the Real or Emulator Device.

#Demo Video link:- https://drive.google.com/file/d/153nxFrrD-4urNS1jsTvFtjqyUm9dyd7c/view?usp=sharing

#Tech Stack used:
1.Kotlin -> For Logic Code
2.Compose -> For UI

#Architecture used:
1.MVVM

Brief About Project:
*This is a Scrollable Vertical Grid PhotoApp in which we are getting random photos from unsplash free version API and then caching every image into Local Disk Space(File Manager) , 
So that on next app laucnh app will render fast by reading data from local cache or also if we do not have internet then also app will work in offline mode
by reading Local stored photos in Disk space.
We have not used any third party library to render photos , We have used Android provided in-built Class Bitmap for all render + caching photos logic.

*We have used MVVM Architecture to build this app using Kotlin as Logic code language and Compose for UI purpose and some of the best practice is used
like StateFlows(To Emit and Collect data every time it changes) , ViewModel to manage data and configuration changes smoothly. 
