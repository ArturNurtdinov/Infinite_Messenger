# EasyMessenger
Simple messenger on Kotlin with Firebase

Project started 15.08.2019

Current version 1.0.3 - 13.09.2019:
1. shrinkResources and minifyEnabled was disable for release version.

# Bug treker
### 1. **(Fixed)** When dialoue started with person, this person sees it as unnamed dialogue without picture. When unnamed item clicked - app crashes.
### 2. **(Fixed)** All images have shadow on it
##### 3. **(Fixed)** Need to collapse dialogues when message too long
##### 4. **(Fixed)** Replace signOut() icon
##### 5. **(Fixed)** Some values don't have translations

# Wish-list
1. **(Added)** Add dark mode
2. Deleting messages in chat.
3. **(Added)** Copy message text
4. Dialogues
5. Pick themes color using palitra.
6. Pick image as a theme in chat
7. Voice messages
8. Indicator if person is online
9. Should write in latest messages when photo sent. Do not show empty message

# Troubleshoot
1. LatestMessageItem has access to database in it
2. Data classes used for fetching and also in app logic
