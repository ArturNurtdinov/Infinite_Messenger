# EasyMessenger
Messenger on Kotlin with Firebase
https://play.google.com/store/apps/details?id=com.topaz.easymessenger

Project started 15.08.2019

Current version 1.1.1 - 01.10.2019:
1. Added emojis
2. Chat log activity changed
3. Notifications removed. Will be added back in future

# Bug treker
### 1. **(Fixed)** When dialoue started with person, this person sees it as unnamed dialogue without picture. When unnamed item clicked - app crashes.
### 2. **(Fixed)** All images have shadow on it
##### 3. **(Fixed)** Need to collapse dialogues when message too long
##### 4. **(Fixed)** Replace signOut() icon
##### 5. **(Fixed)** Some values don't have translations
### 6. **(Fixed)** Sometimes empty messages appear in chat log

# Wish-list
1. **(Added)** Add dark mode
2. Deleting messages in chat.
3. **(Added)** Copy message text
4. Dialogues
5. Pick themes color using palitra.
6. Pick image as a theme in chat
7. Voice messages
8. Indicator if person is online
9. **(Added)** Should write in latest messages when photo sent. Do not show empty message

# Troubleshoot
1. LatestMessageItem has access to database in it
2. Data classes used for fetching and also in app logic
3. **(Done)** Change Picasso lib to Glide
