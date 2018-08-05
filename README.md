# hsbc-twitter

## Compiling

mvn clean install

## Integration tests

mvn clean verify -P integration-test

## Running

 java -jar hsbc-twitter-1.0.0-SNAPSHOT.jar 

## Web Api

### Tweeting

##### Method: POST
 
##### URL: /api/tweet

##### Request body:

```java
public class TweetRequest {

    private String login;
    private String post;
    
    }
```

##### Commandline example

```bash
curl --header "Content-Type: application/json" --request POST --data '{"login":"john_snow","post":"I need your #dragon @khaleesi"}'  http://localhost:8080/api/tweet
```
##### Sample response

```json
{"login":"john_snow","post":"I need your #dragon @khaleesi","dateTime":"2018-08-05T16:54:42.862"}
```

### Wall

##### Method: POST
 
##### URL: /api/wall

##### Request body:

```java
String login;
```

##### Commandline example

```bash
curl --header "Content-Type: application/json" --request POST --data john_snow  http://localhost:8080/api/wall
```

##### Sample response

```json
{"tweets":[{"login":"john_snow","post":"I need your #dragon @khaleesi","dateTime":"2018-08-05T16:23:28.122"}]}t
```
### Follow

##### Method: POST
 
##### URL: /api/follow

##### Request body:

```java
public class FollowRequest {

    private String following;
    private String followed;
    }
```
##### Commandline example

```bash
curl --header "Content-Type: application/json" --request POST --data '{"followed":"john_snow","following":"khaleesi"}'  http://localhost:8080/api/follow
```
### Timeline

##### Method: POST
 
##### URL: /api//timeline

##### Request body:

```java
String login;
```

##### Commandline example

```bash
curl --header "Content-Type: application/json" --request POST --data john_snow  http://localhost:8080/api/wall
```

##### Sample response

```json
{"tweets":[{"login":"john_snow","post":"I need your #dragon @khaleesi","dateTime":"2018-08-05T16:23:28.122"}]}t
```
```