## About project

The project is a reddit-clone.

It uses 

- MySQL as a database.
- Springboot for backend
- Reactjs for web-gui


In the project, user’s can create communities.

These communities can have moderators and administrators.

The administrators can set different rights for the moderators (ex moderators can ban other users, moderators can delete other’s posts)



## Some features

When going to 

```python
http://localhost:3000/
```

the webpage looks like this

![Untitled](https://github.com/Vemund1999/reddit_clone/assets/88531005/b2523b84-5df3-48e7-b278-1458826ba7bc)




You can register a user by clicking “Register”

![register](https://github.com/Vemund1999/reddit_clone/assets/88531005/eb944068-4f75-41cf-ab3c-932349a0c44e)



On the profile page, a user can set a wallpaper and profile image for itself

![4](https://github.com/Vemund1999/reddit_clone/assets/88531005/6b66b931-e256-4c33-8a70-f9eb38c82a2d)


You can make communities, like on reddit.

![5](https://github.com/Vemund1999/reddit_clone/assets/88531005/38ae92a9-bc68-4b25-b629-2a461cd94e9f)


![6](https://github.com/Vemund1999/reddit_clone/assets/88531005/7f224c9d-7949-48be-bf77-90a09513db66)


On these communities, users can make posts
![7](https://github.com/Vemund1999/reddit_clone/assets/88531005/d4f0752e-83ad-41f6-9dd0-3cf50846cc11)


These posts have a threaded-commenting

![8](https://github.com/Vemund1999/reddit_clone/assets/88531005/d0b3f98e-f912-4fb3-9f2c-da47a7fd0fdb)


You will recieve messages for different things, such as getting a reply from another user on your comment.

![9](https://github.com/Vemund1999/reddit_clone/assets/88531005/be7193e5-fb47-4377-8ac3-29dbb2479f4f)


These are topics the things you can recieve messages for

```python
public enum MessageTopic {
    NewReplyToPost,
    NewReplyToComment,
    NewFriendRequest,
    NewRequestToJoinCommunity
}
```

“NewRequestToJoinCommunity” means that on communities that are private or restricted, users must request to join the community

![10](https://github.com/Vemund1999/reddit_clone/assets/88531005/780d8d0a-c2c4-437e-8881-efc2fd941073)


A user can be made moderator by an admin.

The admin can controll what rights the moderators have.

![12](https://github.com/Vemund1999/reddit_clone/assets/88531005/cedd327b-bee6-47bb-801a-6c62d58fd146)


![13](https://github.com/Vemund1999/reddit_clone/assets/88531005/6237dc04-a64e-4b47-8916-66b80d934ff6)


Users can become friends

![14](https://github.com/Vemund1999/reddit_clone/assets/88531005/290dfb53-4e04-4f0d-b0a3-465f7d201f10)

Users have realtime chat.

![15](https://github.com/Vemund1999/reddit_clone/assets/88531005/60fce94d-e278-4e9c-8dee-f0132c3f34a4)




# Setup

Go into the application.properties file under the Reddit-project under backend
```
backend/Reddit-clone/src/main/resources/application.properties
```
Set the path where you want images to be saved. For example:
```python
images.path=/home/username/Pictures/reddit
```

## How to run

In the root of the project, run
```
docker compose up
```
The website will run in the browser at
```
http://localhost:3000
```


## Database
If some of the images are unclear, then click on them to get a better view.
![database](https://github.com/Vemund1999/reddit_clone/assets/88531005/55871bbe-5db8-466b-bb07-2aaec151d00c)


## Backend services
There are two backend services.
An authentication-backend service handles authentication,
while another reddit-backend service handles most of the other logic for the website.
