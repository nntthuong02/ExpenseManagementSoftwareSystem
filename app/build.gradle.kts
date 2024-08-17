plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    ///
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.expensemanagement"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.expensemanagement"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        //

        javaCompileOptions {
            annotationProcessorOptions {
                arguments.put(
                    "room.schemaLocation", "$projectDir/schemas".toString()
                )
                arguments.put(
                    "room.incremental", "true"
                )
                arguments.put(
                    "room.expandProjection", "true"
                )
            }
        }
        //
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.2")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2023.08.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
//    implementation("androidx.compose.material3:material3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.08.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    /************************************/
    implementation("androidx.compose.ui:ui:${rootProject.extra["ui_version"]}")
//    implementation("androidx.compose.material3:material3:1.3.0-beta04")
    implementation("androidx.compose.material3:material3-android:1.2.1")
//    implementation("androidx.compose.foundation:foundation:1.6.7")
    //Constraintlayout
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    // To use constraintlayout in compose
    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")
    //Splash Screen
    implementation("androidx.core:core-splashscreen:1.0.0")
    //Room
    implementation("androidx.room:room-runtime:${rootProject.extra["room_version"]}")
    kapt("androidx.room:room-compiler:${rootProject.extra["room_version"]}")
    implementation("androidx.room:room-ktx:${rootProject.extra["room_version"]}")
    //datastore
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.datastore:datastore:1.1.1")
    //dagger-hilt
    implementation("com.google.dagger:hilt-android:${rootProject.extra["dagger_version"]}")
    kapt("com.google.dagger:hilt-android-compiler:${rootProject.extra["dagger_version"]}")
    //hilt
    kapt("androidx.hilt:hilt-compiler:${rootProject.extra["hilt_version"]}")
    implementation("androidx.hilt:hilt-navigation-fragment:${rootProject.extra["hilt_version"]}")
    implementation("androidx.hilt:hilt-navigation-compose:${rootProject.extra["hilt_version"]}")
    //navigation
    implementation("androidx.navigation:navigation-compose:${rootProject.extra["nav_version"]}")
    //lifecycle viewmodel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${rootProject.extra["lifecycle_version"]}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${rootProject.extra["lifecycle_version"]}")
    //Paging
    implementation("androidx.paging:paging-runtime:${rootProject.extra["paging_version"]}")
    implementation("androidx.paging:paging-compose:3.3.0")
//    appcompact
    //fix Cannot resolve symbol '?attr/colorControlNormal'
    implementation("androidx.appcompat:appcompat:${rootProject.extra["appcompat_version"]}")
    //
    // For loading and tinting drawables on older versions of the platform
    implementation("androidx.appcompat:appcompat-resources:${rootProject.extra["appcompat_version"]}")
}

kapt {
    correctErrorTypes = true
}