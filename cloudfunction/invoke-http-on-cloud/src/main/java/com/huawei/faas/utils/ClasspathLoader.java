/*
 * Copyright 2023. Huawei Technologies Co., Ltd. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.huawei.faas.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ClasspathLoader {
    private static final Logger LOG = LoggerFactory.getLogger("com.huawei.run");

    private static final String[] LAYER_PATH = {"/dcache/layer/lib", "/dcache/layer/config"};

    private static Method addURL = initAddMethod();

    private static URLClassLoader classloader = (URLClassLoader) ClassLoader.getSystemClassLoader();

    private static Method initAddMethod() {
        try {
            Method add = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);
            add.setAccessible(true);
            return add;
        } catch (Exception e) {
            LOG.error("Failed to init add method, {}, stack {}", e.getMessage(), e.getStackTrace());
            return null;
        }
    }

    public static void loadClassPath() throws MyException {
        for (String f : LAYER_PATH) {
            File file = new File(f);
            loopFiles(file);
        }
    }

    private static void loopFiles(File file) throws MyException {
        if (file.isDirectory()) {
            File[] tmps = file.listFiles();
            if (tmps != null) {
                for (File tmp : tmps) {
                    loopFiles(tmp);
                }
            }
        } else {
            if (file.getAbsolutePath().endsWith(".jar") || file.getAbsolutePath().endsWith(".zip")) {
                addURL(file);
            }
        }
    }

    private static void addURL(File file) throws MyException {
        try {
            if (addURL != null) {
                addURL.invoke(classloader, file.toURI().toURL());
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            String errInfo = "Failed to load class " + file.getName();
            throw new MyException(errInfo);
        } catch (MalformedURLException e1) {
            String errInfo = "Failed to load class, malformed URL, " + file.getName();
            throw new MyException(errInfo);
        } catch (Exception e) {
            String errInfo = "Failed to load class, unknown exception, " + file.getName();
            throw new MyException(errInfo);
        }
    }
}