# jhipsterControlCenter

This application was generated using JHipster 7.9.0, you can find documentation and help at [https://www.jhipster.tech/documentation-archive/v7.9.0](https://www.jhipster.tech/documentation-archive/v7.9.0).

## JHipster Control Center

[![Application CI][github-application-ci]][github-actions] [![Docker Pulls][docker-hub-pulls]][docker-hub-url]

[![sonar-quality-gate][sonar-quality-gate]][sonar-url] [![sonar-coverage][sonar-coverage]][sonar-url] [![sonar-bugs][sonar-bugs]][sonar-url] [![sonar-vulnerabilities][sonar-vulnerabilities]][sonar-url]

### Specific Spring profiles

In order to work properly, the Control Center has to be started with a spring profile corresponding to a Spring Cloud discovery backend

- `eureka`: Connect to an Eureka server and fetch its registered instances, configured in `application-eureka.yml`
- `consul`: Connect to a Consul server and fetch its registered instances, configured in `application-consul.yml`
- `static`: Uses a static list of instances provided as properties, configured in `application-static.yml`
- `kubernetes`: To be developed

### Control Center API

- `localhost:7419/api/services/instances`: get registered instances
- `localhost:7419/management/gateway/routes`: get Spring Cloud Gateway routes
- `localhost:7419/gateway/<serviceName>/<instanceName>/<urlPath>`: proxy request to `instanceName`'s urlPath.
  For example, when using Eureka, it would look like: `localhost:7419/gateway/eurekaservice1/eurekaservice1:3d38fb89771e502111b495064d739ef8/management/info`

## Running locally

### Step 1 : Run server used by Spring Cloud discovery backend

Eureka and Consul docker-compose files exist under `src/main/docker` to ease testing the project.

- for Consul : run `docker-compose -f src/main/docker/consul.yml up -d`
- for Eureka : run `docker-compose -f src/main/docker/jhipster-registry.yml up -d`
- Otherwise, to use a static list of instances, you can directly go to the next step.

### Step 2 : Choose your authentication profile

There is 2 types of authentication.

- JWT : This is the default authentication, if you choose this one, you have to do nothing.
- OAuth2 : To use OAuth2 authentication, you have to launch Keycloak. Run `docker-compose -f src/main/docker/keycloak.yml up -d`

### Step 3 : Run the cloned project

Run the Control Center according to the specific spring profiles you want, here are some examples:

- For development with JWT and Consul, run ./mvnw -Dspring.profiles.active=consul,dev
- For development with JWT and Eureka, run./mvnw -Dspring.profiles.active=eureka,dev
- For development with JWT and a static list of instances, run ./mvnw -Dspring.profiles.active=static,dev
- For development with OAuth2 and Consul, run ./mvnw -Dspring.profiles.active=consul,dev,oauth2
- For development with OAuth2 and Eureka, run ./mvnw -Dspring.profiles.active=eureka,dev,oauth2
- To just start in development run ./mvnw and in another terminal run npm start for hot reload of client side code

## Running from Docker

A container image has been made available on Docker hub.To use it, run `docker pull jhipster/jhipster-control-center` and `docker run -d --name jhcc -p 7419:7419 jhipster/jhipster-control-center:latest`

## Project Structure

Node is required for generation and recommended for development. `package.json` is always generated for a better development experience with prettier, commit hooks, scripts and so on.

In the project root, JHipster generates configuration files for tools like git, prettier, eslint, husky, and others that are well known and you can find references in the web.

`/src/*` structure follows default Java structure.

- `.yo-rc.json` - Yeoman configuration file
  JHipster configuration is stored in this file at `generator-jhipster` key. You may find `generator-jhipster-*` for specific blueprints configuration.
- `.yo-resolve` (optional) - Yeoman conflict resolver
  Allows to use a specific action when conflicts are found skipping prompts for files that matches a pattern. Each line should match `[pattern] [action]` with pattern been a [Minimatch](https://github.com/isaacs/minimatch#minimatch) pattern and action been one of skip (default if ommited) or force. Lines starting with `#` are considered comments and are ignored.
- `.jhipster/*.json` - JHipster entity configuration files

- `npmw` - wrapper to use locally installed npm.
  JHipster installs Node and npm locally using the build tool by default. This wrapper makes sure npm is installed locally and uses it avoiding some differences different versions can cause. By using `./npmw` instead of the traditional `npm` you can configure a Node-less environment to develop or test your application.
- `/src/main/docker` - Docker configurations for the application and services that the application depends on

## Development

Before you can build this project, you must install and configure the following dependencies on your machine:

1. [Node.js][]: We use Node to run a development web server and build the project.
   Depending on your system, you can install Node either from source or as a pre-packaged bundle.

After installing Node, you should be able to run the following command to install development tools.
You will only need to run this command when dependencies change in [package.json](package.json).

```
npm install
```

We use npm scripts and [Webpack][] as our build system.

Run the following commands in two separate terminals to create a blissful development experience where your browser
auto-refreshes when files change on your hard drive.

```
./mvnw
npm start
```

Npm is also used to manage CSS and JavaScript dependencies used in this application. You can upgrade dependencies by
specifying a newer version in [package.json](package.json). You can also run `npm update` and `npm install` to manage dependencies.
Add the `help` flag on any command to see how you can use it. For example, `npm help update`.

The `npm run` command will list all of the scripts available to run for this project.

### PWA Support

JHipster ships with PWA (Progressive Web App) support, and it's turned off by default. One of the main components of a PWA is a service worker.

The service worker initialization code is commented out by default. To enable it, uncomment the following code in `src/main/webapp/index.html`:

```html
<script>
  if ('serviceWorker' in navigator) {
    navigator.serviceWorker.register('./service-worker.js').then(function () {
      console.log('Service Worker Registered');
    });
  }
</script>
```

Note: [Workbox](https://developers.google.com/web/tools/workbox/) powers JHipster's service worker. It dynamically generates the `service-worker.js` file.

### Managing dependencies

For example, to add [Leaflet][] library as a runtime dependency of your application, you would run following command:

```
npm install --save --save-exact leaflet
```

To benefit from TypeScript type definitions from [DefinitelyTyped][] repository in development, you would run following command:

```
npm install --save-dev --save-exact @types/leaflet
```

Then you would import the JS and CSS files specified in library's installation instructions so that [Webpack][] knows about them:
Note: There are still a few other things remaining to do for Leaflet that we won't detail here.

For further instructions on how to develop with JHipster, have a look at [Using JHipster in development][].

## Building for production

### Packaging as jar

To build the final jar and optimize the jhipsterControlCenter application for production, run:

```
./mvnw -Pprod clean verify
```

This will concatenate and minify the client CSS and JavaScript files. It will also modify `index.html` so it references these new files.
To ensure everything worked, run:

```
java -jar target/*.jar
```

Then navigate to [http://localhost:7419](http://localhost:7419) in your browser.

Refer to [Using JHipster in production][] for more details.

### Packaging as war

To package your application as a war in order to deploy it to an application server, run:

```
./mvnw -Pprod,war clean verify
```

## Testing

To launch your application's tests, run:

```
./mvnw verify
```

### Client tests

Unit tests are run by [Jest][]. They're located in [src/test/javascript/](src/test/javascript/) and can be run with:

```
npm test
```

UI end-to-end tests are powered by [Cypress][]. They're located in [src/test/javascript/cypress](src/test/javascript/cypress)
and can be run by starting Spring Boot in one terminal (`./mvnw spring-boot:run`) and running the tests (`npm run e2e`) in a second one.

#### Lighthouse audits

You can execute automated [lighthouse audits][https://developers.google.com/web/tools/lighthouse/] with [cypress audits][https://github.com/mfrachet/cypress-audit] by running `npm run e2e:cypress:audits`.
You should only run the audits when your application is packaged with the production profile.
The lighthouse report is created in `target/cypress/lhreport.html`

For more information, refer to the [Running tests page][].

### Code quality

Sonar is used to analyse code quality. You can start a local Sonar server (accessible on http://localhost:9001) with:

```
docker-compose -f src/main/docker/sonar.yml up -d
```

Note: we have turned off authentication in [src/main/docker/sonar.yml](src/main/docker/sonar.yml) for out of the box experience while trying out SonarQube, for real use cases turn it back on.

You can run a Sonar analysis with using the [sonar-scanner](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner) or by using the maven plugin.

Then, run a Sonar analysis:

```
./mvnw -Pprod clean verify sonar:sonar
```

If you need to re-run the Sonar phase, please be sure to specify at least the `initialize` phase since Sonar properties are loaded from the sonar-project.properties file.

```
./mvnw initialize sonar:sonar
```

For more information, refer to the [Code quality page][].

## Using Docker to simplify development (optional)

You can use Docker to improve your JHipster development experience. A number of docker-compose configuration are available in the [src/main/docker](src/main/docker) folder to launch required third party services.

You can also fully dockerize your application and all the services that it depends on.
To achieve this, first build a docker image of your app by running:

```
npm run java:docker
```

Or build a arm64 docker image when using an arm64 processor os like MacOS with M1 processor family running:

```
npm run java:docker:arm64
```

Then run:

```
docker-compose -f src/main/docker/app.yml up -d
```

When running Docker Desktop on MacOS Big Sur or later, consider enabling experimental `Use the new Virtualization framework` for better processing performance ([disk access performance is worse](https://github.com/docker/roadmap/issues/7)).

For more information refer to [Using Docker and Docker-Compose][], this page also contains information on the docker-compose sub-generator (`jhipster docker-compose`), which is able to generate docker configurations for one or several JHipster applications.

## Continuous Integration (optional)

To configure CI for your project, run the ci-cd sub-generator (`jhipster ci-cd`), this will let you generate configuration files for a number of Continuous Integration systems. Consult the [Setting up Continuous Integration][] page for more information.

[jhipster homepage and latest documentation]: https://www.jhipster.tech
[jhipster 7.9.0 archive]: https://www.jhipster.tech/documentation-archive/v7.9.0
[using jhipster in development]: https://www.jhipster.tech/documentation-archive/v7.9.0/development/
[using docker and docker-compose]: https://www.jhipster.tech/documentation-archive/v7.9.0/docker-compose
[using jhipster in production]: https://www.jhipster.tech/documentation-archive/v7.9.0/production/
[running tests page]: https://www.jhipster.tech/documentation-archive/v7.9.0/running-tests/
[code quality page]: https://www.jhipster.tech/documentation-archive/v7.9.0/code-quality/
[setting up continuous integration]: https://www.jhipster.tech/documentation-archive/v7.9.0/setting-up-ci/
[node.js]: https://nodejs.org/
[npm]: https://www.npmjs.com/
[webpack]: https://webpack.github.io/
[browsersync]: https://www.browsersync.io/
[jest]: https://facebook.github.io/jest/
[cypress]: https://www.cypress.io/
[leaflet]: https://leafletjs.com/
[definitelytyped]: https://definitelytyped.org/

https://medium.com/@pubuduc.14/swagger-openapi-specification-3-integration-with-spring-cloud-gateway-part-2-1d670d4ab69a
https://springdoc.org/index.html#demos
https://github.com/springdoc/springdoc-openapi-demos/tree/master
