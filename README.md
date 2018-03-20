# pushnotification-android
This project is created to test and verify few things related to FCM Server-Android implementation.


If you want to run in your system there are few things that you need to setup before running the project.

1. Create Fiebase project using [firebase console](https://console.firebase.google.com/)
2. Download and add<b> google-services.json</b> in the project. (Follow the instructions while creating the project)
3. Enable Email/Password signin [follow this link](https://firebase.google.com/docs/auth/android/password-auth)
    <ol type='a'><li>[Firebase Console](https://console.firebase.google.com/) and open the auth section</li><li>In the sign-in method tab enable email/password option and save.</li></ol>
4. Go to <b>Settings Icon</b> in front of <b>Project Overview</b>
5. Move to <b> Cloud Messaging </b> tab and copy the sender ID and paste it in [Keys.java](https://github.com/lakshyagupta21/pushnotification-android/blob/master/pushnotificationandroid/app/src/main/java/com/dexter/pushnotificationandroid/Keys.java)
