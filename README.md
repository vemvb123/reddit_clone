## About project

The project is a reddit-clone.

It uses 

- MySQL as a database.
- Springboot for backend
- Reactjs for web-gui


In the project, user’s can create communities.

These communities can have moderators and administrators.

The administrators can set different rights for the moderators (ex moderators can ban other users, moderators can delete other’s posts)

## How to run

In the springboot project, change the [application.properties](http://application.properties) to appropriate values for the mysql database.

```python
# JPA configuration
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:port/database_name
spring.datasource.username=username
spring.datasource.password=password
spring.jpa.show-sql=true

spring.jpa-properties.hibernate.dialect=org.hibernate.dialect.MySQL5Dialect
spring.main.allow-bean-definition-overriding=true
spring.jpa.properties.hibernate.show_sql=false

# Set the path to save images
pathToSaveImages=C:/Users/path_to_save_images

# file configuration
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB

logging.level.org.springframework.web: DEBUG
```

Make a database in mysql with the correct database_name.

Also make sure to specify at what path you want images to be saved at.

Run the file 

```python
Reddit clone\Reddit-clone\src\main\java\com\example\Reddit\clone\RedditCloneApplication.java
```

Then run the reactjs project with 

```python
npm install
npm update
npm start
```

Go to

```python
http://localhost:3000/
```

to see the webpage

## Database
If some of the images are unclear, then click on them to get a better view.
![database](https://github.com/Vemund1999/reddit_clone/assets/88531005/55871bbe-5db8-466b-bb07-2aaec151d00c)



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

## Potential improvments
The project can be improved further by splitting the project into micro-services. Each micro-service can be in a docker-contrainer. There can be micro-services for users, communities & comments & posts. threaded commenting is currently handled using SQL. It would probably be easier to handle this with mongo-db. Mongo-db can have nested documents, which reminds more of nested comments.

