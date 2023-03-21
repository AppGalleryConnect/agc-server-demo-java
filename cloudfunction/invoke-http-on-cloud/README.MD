## invoke-http-function-on-cloud Quickstart

## Introduction
invoke-http-function-on-cloud demo help you quickly build your cloud function(custom-runtime). Invoking http function on the cloud refers to invoking APIs using SDKs in the java-runtime or custom-runtime running environment of cloud functions.

## Preparing the Environment
Before using the sdk, your server need support Java 8 or higher.

## Getting Started
1. Create an HTTP request cloud function named "called".(Refer to the "custom-runtime-function-demo" project)
2. Download and update the authentication credential file "agc-apiclient-key.json" in the /resources directory.
3. Fill in the called functionName and version in com.huawei.faas.service#Line43.
4. Enter the HttpMethod, url, headers and requestBody in com.huawei.faas.service#Line61.
5. Build this invoke-http-function-on-cloud maven project.
6. Create an HTTP request cloud function named "invoke-http-function-on-cloud" and upload code file in target directory.
7. Invoke "invoke-http-function-on-cloud" function.

## Build Help
1. Java language projects can be managed by using Maven commands.
2. Scripts for generating ZIP files have been compiled in the zip_file.xml and pom.xml files. 
3. After the project is compiled, you can run the mvn package command to automatically package the project into the preceding structure.
4. The generated xx.zip package is stored in the /target directory generated after compilation.

## Demo
![demo](./png/demo.png)

