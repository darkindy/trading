# Trading Application

This project is a Trading Application that executes Trading Algorithm logic when given a signal. The Trading
Algorithm library containing the SignalHandler and Algo classes is provided and cannot be modified. The goal of this
project is to implement the new signal specifications given in the form of JIRA tickets and release them to production
as quickly as possible.

## Build Status
![example workflow](https://github.com/darkindy/trading/actions/workflows/maven.yml/badge.svg)

## Requirements

The code for the Trading Application needed to be modified to make it easier to understand, maintain, and test. The
following requirements should be met:

- The code should be easy to understand and debug.
- The code should be easy to maintain and add new signals.
- The code should have appropriate levels of testing to ensure that the requirements are met.

## Getting Started

To get started with the Trading Application, follow these steps:

1. Clone the repository from GitHub.
2. Open the project in your preferred IDE.
3. Run `./mvnw clean install` to build the application.
4. Run the application with `java -jar trading-application/target/trading-application-0.0.1-SNAPSHOT.jar`.
5. The application should now be running and ready to receive signals.

## Usage

The Trading Application has a single HTTP endpoint that can receive a signal. The signal is passed through the
SignalHandler interface and onto the application. To send a signal, make a POST request
to `http://localhost:8080/api/signals/{id}` with an integer value specifying the signal id.

## Conclusion

The Trading Application is a simple but important tool for executing Trading Algorithm logic. By implementing the new
signal specifications quickly and efficiently, the application can help traders make better decisions and achieve better
results.
