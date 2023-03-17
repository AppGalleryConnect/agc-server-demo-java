## invoke-http-function-in-local Quickstart

## Introduction
invoke-http-function-in-local demo help you quickly build your project.

## Preparing the Environment
Before using the sdk, your server need support Java 8 or higher.

## Getting Started
1. Create an HTTP request cloud function named "called".(Refer to the "custom-runtime-function-demo" project)
2. Download and update the authentication credential file "agc-apiclient-key.json" in the /resources directory.
3. Fill in the projectId, clientId and clientSecret in com.huawei.faas#Line32.
4. Fill in the HttpMethod, url, headers and requestBody in com.huawei.faas#Line36.
5. Update agcApiClientKeyPath in com.huawei.faas#Line45.
6. Fill in the functionName and version in com.huawei.faas#Line51.
7. Run this invoke-http-function-in-local maven project.