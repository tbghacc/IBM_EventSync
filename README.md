# IBM Internship Exercise: Event Feedback Analyzer

SpringBoot app made for IBM Internship, 2025 Summer

## Running the app

### Setup

For AI sentiment analysis to work, you're going to need an HuggingFace Access token.
You can get one by:
1. Creating a HuggingFace account
2. Going to Access Tokens (or Settings -> Access Tokens)
3. Creating a new token (Make sure to mark *"Make calls to Inference Providers"*)

Once you have this token you're going to want to create a secrets.properties file in the root directory (same place where this README is)
Inside of secrets.properties you should put:
```
HF_TOKEN=(Your HuggingFace Access token here)
```

## Database

The database is configured to create files locally.
It will create a folder called "data" in the project directory with a couple predefined tables.
This was done as to not waste the precious "Interface Usage" on HuggingFace.

You can access it by going to
```
http://localhost:8080/h2-console
```

And then inputting
```
Driver Class: org.h2.Driver
JDBC URL: jdbc:h2:file:./data/EventSyncDB
User Name: sa
```

## Building + Starting

This app is built using gradle, so all standard commands apply

Main command line operation:

- Start the App: `./gradlew bootRun`
- Build the App: `./gradlew build`

I have also included a Postman collection which includes all of the API calls you can make, you can find it in the postman folder.

