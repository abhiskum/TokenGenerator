## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```

## Packaging and running the application

The application can be packaged using `./mvnw package`.
It produces the `token-generator-1.0.0-runner.jar` file in the `/target` directory.
Be aware that it’s an _über-jar_.

The application is now runnable using `java -jar target/token-generator-1.0.0-runner.jar`.

## Creating a native executable

You can create a native executable using: `./mvnw package -Pnative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./mvnw package -Pnative -Dquarkus.native.container-build=true`.

You can then execute your native executable with: `./target/token-generator-1.0.0-runner`

## Building and run native docker image
```
docker build -f src/main/docker/Dockerfile.native -t abhiskum/gs-token-generator .
docker run -i --rm --name token-generator --env PORT=8080 -p 80:8080 abhiskum/gs-token-generator
```

## Building and run jvm docker image
Default timezone set in this Dockerfile.jvm is IST.
```
docker build -f src/main/docker/Dockerfile.jvm -t abhiskum/gs-token-generator .
docker run -i --rm --name token-generator --env PORT=8080 -p 80:8080 abhiskum/gs-token-generator
```
## Adding new client
Client configuration details in application.properties file

```
clients=<comma seperated list of client id>

<client_id>.items=<commoa seperated list of items>

For each item
<client_id>.item.<item_name>.display.name=<item display name>
<client_id>.item.<item_name>.slot.duration=<slot interval>
<client_id>.item.<item_name>.person.per.slot=<person per slot>
<client_id>.item.<item_name>.token.start.time=<hours of day> or <hours of day:minitues>
<client_id>.item.<item_name>.sell.start.time=<hours of day> or <hours of day:minitues>
<client_id>.item.<item_name>.sell.end.time=<hours of day> or <hours of day:minitues>
```

## Deploy on Heroku
```
./mvnw clean install
docker build -f src/main/docker/Dockerfile.jvm -t <application name> .

heroku login
heroku create <application name>
docker tag <application name>:latest registry.heroku.com/<application name>/web
docker push registry.heroku.com/<application name>/web
heroku container:release web -a <application name>
heroku  logs  --app <application name>
```