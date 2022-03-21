# *Simple Contacts*

**SimpleContacts** is an android app that allows any user to sign in (via Google sign-in) and create, read, update, and delete contact information to a personalized contacts list stored in a Google Firebase Cloud Firestore.

## User Stories

The following **main** functionality is implemented:

- [x] User can **sign in to Firebase** using Google sign-in
- [x] User can **view a list of contacts stored in Firebase**
- [x] User can **add** a new contact
- [x] User *is displayed** the *Name, Phone Number(s), e-mail Address, Address, and Gender* for each contact
- [x] User can **update** an existing contact
- [x] User can **delete** a new contact
- [x] User can scroll through the list of contacts if not all contacts are able to be displayed on-screen simultaneously

The following **additional** features are implemented:

- [x] First time users are taken directly into the app and may start creating contacts right away without the need for registration
- [x] Multiple users are supported, each with their own unique list of contacts
- [x] Empty contact details are not shown in the MainActivity
- [x] The delete button is not shown for a new contact
- [x] User is prompted to confirm before deleting a contact, or discarding changes
- [x] UI & Visibility enhancements

## Video Walkthrough

Here's a walkthrough of the login process:

<img src='https://github.com/oneparchy/Simplecontacts/blob/master/SimpleContacts_login.gif' title='Login Example' width='' alt='Login Example' />

Here's a walkthrough of implemented user stories:

<img src='https://github.com/oneparchy/Simplecontacts/blob/master/SimpleContacts_demo.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

Note
- Google Services API above 4.3.5 does not work for whatever reason, it will not detect the Firebase config file.
  Stick to 4.3.5 until a solution is discovered. (https://stackoverflow.com/questions/37810552/cannot-resolve-symbol-default-web-client-id-in-firebases-android-codelab)
- You will need your own Firebase config file (google-services.json) to implement an independent version of this app. (https://firebase.google.com/docs/android/setup#add-config-file)

## Open-source libraries used

- [Firebase UI for Android](https://github.com/firebase/FirebaseUI-Android) - Open-source UI Bindings for Firebase

## License

    Copyright [2022] [https://github.com/oneparchy]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
