# Cup_of_Tea #

Jake Jin, Harry Barng, Liuqing Ma, Wonjo Barng

## Demo Video (iOS and Android)
[![YouTube Demo Video](https://img.youtube.com/vi/3cB3bl6wHlI/0.jpg)](https://www.youtube.com/watch?v=3cB3bl6wHlI)

## Instruction/User manual ##
A dating app based on realtime Firebase DB. Go ahead and find the one!

1. Setup the profile (~7 screens)
2. Once uploaded, click the person you like (Master/detail screen with burger menu)
3. See their profile (detail fragment)
4. If you want to contact them, click Connect via email/phone (intent to other apps)
5. Or navigate to them (intent to google map)
6. If you don't want to share your location, turn off "Share My Location" in the menu (swicth on burger menu, update in realtime DB)
7. click profile to see your profile (1 screen triggered by burger menu)
8. click reset in profile screen to change/reset your profile (start over button)

## Purpose ##
Dating apps have a huge market as everyone has demand of dating and looking for true love. An online dating app through the mobile context will be more convenient and faster for people to find someone they like whenever they are. Dating should be fun and healthy, but current online dating solutions can be frustrating. Some weaknesses such as unlimited matches, older community, weak sharing interests make it harder for UW community to find true love. We will build a new dating app ‘Cup of Coffee’ targeting to UW current students and alumnus, with following functionalities:
Setup my profile
* Browse a list of people near me
* View the detailed profile of the one I’m interested in
* Get each other’s current geo location and see how far we are
* Easily message to the one I selected

## Mobile Sensors/features used ##

1. Request Location and calculate distance based on lat and lng, user can choose to hide location later.
2. Request Camera and/or Photo Album to upload a profile picture (to Firebase).
3. Send intent to message/mail for communication, to Google maps for navigation and location viewing.
4. Master/detail view to display a list of available people. Try landscape mode on the list!
5. Manage my privacy settings(especially location) and view/reset my profile.

## User Story ##
1. As a user, I want to take a picture of me and insert my interests so that I can make myprofile attractive
2. As a user, I want to view a list of people near me so I can browse potential girl/boy I wantto date
3. As a user, I want to easily view profile details so I can determine who catches my eyes
4. As a user, I want to send message to the one I’m interested in so that I can connect andchat with him/her
5. As a user, I want to see how far they are from me
6. As a user, I want to allow/request navigation between me and the other user.
