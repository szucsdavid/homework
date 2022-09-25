# Trader Test

The goal of this project is to test the Trader application

## How to run:

### Running with Docker:

`docker run -it -v ${YOUR_PATH}/homework/:/homework/ szdavid/homework test`

Change **YOUR PATH** to the path where you checked out from git

Report.html will be created into the TestReport folder

Alternatively you can build the docker image yourself with the provided Dockerfile

`docker build -t homework .`

`docker run -it -v ${YOUR_PATH}/homework/:/homework/ szdavid/homework test`

### Running on your local machine:

Standing in the project root folder with maven installed and setup

`mvn test`

Or just use your favorite IDE to handle the maven project and the execution. :)

### Test case scenarios

Additional test case scenarios can be found in the: 

`./TestCaseScenarios.txt`

file