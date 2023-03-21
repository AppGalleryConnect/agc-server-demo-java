## 本地调用云函数示例 快速入门

## 介绍
本项目帮助您在开发环境本地调用云函数。

## 环境准备
您的服务器需要支持Java8或者更高版本

## 快速开始
1. 创建一个函数名为"called"，函数类型为"http请求"的云函数。（参考"custom-runtime-function-demo"项目）
2. 下载并更新/resources目录下的认证凭据文件"agc-apiclient-key.json"。
3. 在com.huawei.faas 32行中填写被调函数所在项目的projectId、clientId和clientSecret。
4. 在com.huawei.faas 36行中填写自定义HttpMethod、自定义url、自定义headers和自定义requestBody。
5. 在com.huawei.faas 45行中填写apg客户端秘钥。
6. 在com.huawei.faas 51行中填写被调函数的函数名和版本。
7. 运行main函数，在本地调用名为"called"的云函数。