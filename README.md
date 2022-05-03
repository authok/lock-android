Lock for Android
============
[![CircleCI](https://circleci.com/gh/authok/lock-android.svg?style=shield)](https://circleci.com/gh/authok/lock-android)
[![License](https://img.shields.io/dub/l/vibe-d.svg?style=flat)](https://opensource.org/licenses/MIT)
[![Maven Central](https://img.shields.io/maven-central/v/cn.authok.android/lock.svg)](https://search.maven.org/#search%7Cga%7C1%7Cg%3A%cn.authok.android%22%20AND%20a%3A%22lock%22)
[![Download](https://api.bintray.com/packages/authok/android/lock/images/download.svg)](https://bintray.com/authok/android/lock/_latestVersion)

[AuthOK](https://authok.cn) is an authentication broker that supports social identity providers as well as enterprise identity providers such as Active Directory, LDAP, Google Apps and Salesforce.

## 关键特性

* **Integrates** your Android app with **AuthOK**.
* Provides a **beautiful native UI** to log your users in.
* Provides support for **Social Providers** (Facebook, Twitter, etc.), **Enterprise Providers** (AD, LDAP, etc.) and **Username & Password**.
* 提供了 **短信** 和 **邮箱** 等免密认证.

## Users from lock-android v2
If you are migrating from the previous major version of this library, make sure to check out the [Migration Guide](MIGRATION_GUIDE.md) we prepared for you.

## Notes On Embedded Authentication

Since June 2017 new Applications no longer have the **Password Grant Type** enabled by default. If you are using a Database Connection in Lock then you will need to enable the Password Grant Type, please follow [this guide](https://docs.authok.cn/docs/clients/client-grant-types#how-to-edit-the-client-grant_types-property). The reasons for this change are listed in this [embedded vs native mobile article](https://docs.authok.cn/docs/tutorials/browser-based-vs-native-experience-on-mobile).

## Requirements

Android API Level 21+ & Java version 8 or above is required in order to use Lock's UI.

Here’s what you need in build.gradle to target Java 8 byte code for the Android and Kotlin plugins respectively.

```groovy
android {
    compileOptions {
        sourceCompatibility JavaVersion.VERSION_1_8
        targetCompatibility JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = '1.8'
    }
}
```

## 安装

Lock is available in [Maven Central](http://search.maven.org). To start using *Lock* add this line to the dependencies of your `build.gradle` file:

```groovy
implementation 'cn.authok:lock-android:1.0.0'
```

## 使用

If you haven't done yet, go to [Authok](https://authok.cn) and create an Account, it's free! Then create a new [Application](https://mgmt.authok.cn/app/applications) of type *Native* and add a URL in *Allowed Callback URLs* with the following format:

```
https://{YOUR_AUTHOK_DOMAIN}/android/{YOUR_APP_PACKAGE_NAME}/callback
```

The *package name* value required in the Callback URL can be found in your app's `build.gradle` file in the `applicationId` property. Both the *domain* and *client id* values can be found at the top of your AuthOK Application's settings. You're going to use them to setup the SDK. It's good practice to add them to the `strings.xml` file as string resources that you can reference later from the code. This guide will follow that practice.

```xml
<resources>
    <string name="cn_authok_client_id">YOUR_AUTHOK_CLIENT_ID</string>
    <string name="cn_authok_domain">YOUR_AUTHOK_DOMAIN</string>
</resources>
```

In your `app/build.gradle` file add the **Manifest Placeholders** for the AuthOK Domain and AuthOK Scheme properties, which are going to be used internally by the library to declare the Lock activities and register **intent-filters** that will capture the authentication result.

```groovy
apply plugin: 'com.android.application'

android {
    compileSdkVersion 30
    defaultConfig {
        applicationId "cn.authok.samples"
        minSdkVersion 21
        targetSdkVersion 30
        //...

        //---> Add the next line
        manifestPlaceholders = [authokDomain: "@string/cn_authok_domain", authokScheme: "https"]
        //<---
    }
    //...
}
```

The next step is to create an instance of `Authok` with your application's information. The easiest way to create it is by using the values defined previously in the `strings.xml` file and passing an Android Context. For this to work, you must have defined the string resources using the same keys as listed above.

```kotlin
val account = Authok(context)
```

Alternatively, you can directly pass the values.

```kotlin
val account = Authok("{YOUR_AUTHOK_CLIENT_ID}", "{YOUR_AUTHOK_DOMAIN}")
```

Or, if you are using _custom domains_ and are required to specify a different URL to fetch the Lock widget configuration from, you can use the constructor that takes 3 parameters:

```kotlin
val account = Authok("{YOUR_AUTHOK_CLIENT_ID}", "{YOUR_AUTHOK_DOMAIN}", "{THE_CONFIGURATION_DOMAIN}")
```


### 邮箱/用户名/手机号/密码, 企业级 & 社交认证

Initialize **Lock** and handle the release of its resources appropriately after you're done using it. 

```kotlin
// This activity will show Lock
class MyActivity : AppCompatActivity() {

    private lateinit var lock: Lock

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    
        val account = Authok(this)
        // Instantiate Lock once
        lock = Lock.newBuilder(account, callback)
            // Customize Lock
            .build(this)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // Important! Release Lock and its resources
        lock.onDestroy(this)
    }
    
    private val callback = object : AuthenticationCallback() {
        override fun onAuthentication(credentials: Credentials) {
            // Authenticated
        }
        
        override fun onError(error: AuthenticationException) {
            // An exception occurred
        }
    }
}
```

Start `LockActivity` from inside your `Activity`. For this, create a new intent from the Lock instance and launch it.

```kotlin
startActivity(lock.newIntent(this))
```

### Passwordless & Social authentication

The Passwordless feature requires your Application to have the *Passwordless OTP* Grant Type enabled first. Check [this article](https://docs.authok.cn/docs/clients/client-grant-types) to learn how to enable it. 

`PasswordlessLockActivity` authenticates users by sending them an Email or SMS (similar to how WhatsApp authenticates you). In order to be able to authenticate the user, your application must have the SMS/Email connection enabled and configured in your [dashboard](https://mgmt.authok.cn/connections/passwordless).

Initialize **PasswordlessLock** and handle the release of its resources appropriately after you're doing using it. 

```kotlin
// This activity will show PasswordlessLock
class MyActivity : AppCompatActivity() {
    
    private lateinit var lock: PasswordlessLock
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    
        val account = Authok(this)
        // Instantiate Lock once
        lock = PasswordlessLock.newBuilder(account, callback)
            // Customize Lock
            .build(this)
    }
    
    override fun onDestroy() {
        super.onDestroy()
        // Important! Release Lock and its resources
        lock.onDestroy(this)
    }
    
    private val callback = object : AuthenticationCallback() {
        override fun onAuthentication(credentials: Credentials) {
            // Authenticated
        }
        
        override fun onError(error: AuthenticationException) {
            // An exception occurred
        }
    }
}
```

Start `PasswordlessLockActivity` from inside your `Activity`. For this, create a new intent and launch it.

```kotlin
startActivity(lock.newIntent(this))
```

### Customizing the widget

When using the `Builder` to instantiate the widget, you can pass different options to customize how it will behave. Some options are only available for **Lock** or **PasswordlessLock**. Below you will find a few of them. You can always explore all the available options with your IDE's auto-complete shortcut. Check the Javadocs to understand the default values.


```kotlin
// Create a new builder from Lock or LockPasswordless classes
newBuilder(account, callback)
    // Shared options
    .closable(true) // Allows the widget to be closed with the back button
    .withScope('new-scope') // Changes the scope to be requested on authentication
    .withAudience('my-api') // Changes the audience to be requested on authentication
    .withScheme('myapp') // Changes the scheme part used to generate the Callback URL (more below)
    
    // Lock specific options
    .initialScreen(InitialScreen.SIGN_UP) // Allows to choose the screen to be displayed first 
    .allowLogIn(false) // Disables the Log In screen
    .allowSignUp(false) // Disables the Sign Up screen
    .allowForgotPassword(false) // Disables the Change Password screen
    .setDefaultDatabaseConnection('my-connection') // When multiple are available, select one
    
    // PasswordlessLock specific options
    .useCode(true)  // Requests to receive a OTP that will need to be filled in your android app to authenticate the user
    .useLink(false) // Requests to receive a link that will open your android app to authenticate the user
    .rememberLastLogin(true) // Saves the email or phone number to avoid re-typing it in the future
    
    // Build the instance
    .build(this)
```

#### Android App Links - Custom Scheme
The default scheme used by the library to generate the Callback URL for Web Authentication is `https`. This works best for Android Marshmallow (API 23) or newer if you're using [Android App Links](https://developer.android.com/training/app-links/index.html). However, in previous Android versions, this may show the disambiguation dialog prompting the user to choose either your application or the browser to resolve the intent. You can change this behavior by using a unique custom scheme so that the OS opens the link directly with your app.

1. Update the `authokScheme` Manifest Placeholder value in the `app/build.gradle` file or directly in the Intent Filter definition in the `AndroidManifest.xml` file by changing the existing scheme to the new one.
2. Update the "Allowed Callback URLs" in your [Authok Dashboard](https://mgmt.authok.cn/app/applications) Application's settings to match URLs that begin with the new scheme.
3. Call `withScheme()` in the Lock.Builder/PasswordlessLock.Builder passing the scheme you want to use.

> The scheme value **must** be all lowercase. A warning message will be logged if this is not the case and authentication will never complete.

## Proguard
The rules should be applied automatically if your application is using `minifyEnabled = true`. If you want to include them manually check the [proguard directory](proguard).
By default you should at least use the following files:
* `proguard-gson.pro`
* `proguard-otto.pro`
* `proguard-lock-2.pro`

As this library depends on `authok-android`, you should keep the files up to date with the proguard rules defined in the SDK [repository](https://github.com/authok/authok-android).

## What is Authok?

Authok helps you to:

* Add authentication with [multiple authentication sources](https://docs.authok.cn/identityproviders), either social like **Google, Facebook, Microsoft Account, LinkedIn, GitHub, Twitter, Box, Salesforce, among others**, or enterprise identity systems like **Windows Azure AD, Google Apps, Active Directory, ADFS or any SAML Identity Provider**.
* Add authentication through more traditional **[username/password databases](https://docs.authok.cn/mysql-connection-tutorial)**.
* Add support for **[linking different user accounts](https://docs.authok.cn/link-accounts)** with the same user.
* Support for generating signed [Json Web Tokens](https://docs.authok.cn/jwt) to call your APIs and **flow the user identity** securely.
* Analytics of how, when and where users are logging in.
* Pull data from other sources and add it to the user profile, through [JavaScript rules](https://docs.authok.cn/rules).

## Create a free account in Authok

1. Go to [Authok](https://authok.cn) and click Sign Up.
2. Use Google, GitHub or Microsoft Account to login.

## Issue Reporting

If you have found a bug or if you have a feature request, please report them at this repository issues section. Please do not report security vulnerabilities on the public GitHub issue tracker. The [Responsible Disclosure Program](https://authok.cn/whitehat) details the procedure for disclosing security issues.

## Author

[Authok](https://authok.cn)

## License

This project is licensed under the MIT license. See the [LICENSE](LICENSE.md) file for more info.
