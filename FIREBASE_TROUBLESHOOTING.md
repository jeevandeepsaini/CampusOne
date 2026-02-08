# 🔥 Firebase Issues & Fixes - CampusOne

## 🚨 Common Firebase Issues and Solutions

---

## 1️⃣ GOOGLE-SERVICES.JSON ISSUES

### **Issue 1.1: "File google-services.json is missing"**

**Error:**
```
File google-services.json is missing. The Google Services Plugin cannot function without it.
```

**Solution:**
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Select your project
3. Click ⚙️ (Settings) → Project settings
4. Scroll to "Your apps" section
5. Click on your Android app
6. Click "Download google-services.json"
7. Place file at: `CampusOne/app/google-services.json`
8. Sync Gradle

**Verify Placement:**
```
CampusOne/
  app/
    google-services.json  ← Should be here!
    build.gradle.kts
    src/
```

---

### **Issue 1.2: "google-services plugin not applied"**

**Error:**
```
Could not find method implementation() for arguments [com.google.firebase:firebase-auth]
```

**Solution:**
Check `app/build.gradle.kts` has:

```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")  // ← Must be here!
}
```

**And** check `build.gradle.kts` (project-level) has:

```kotlin
plugins {
    // ...existing plugins...
    id("com.google.gms.google-services") version "4.4.0" apply false
}
```

---

## 2️⃣ FIREBASE AUTHENTICATION ISSUES

### **Issue 2.1: "We have blocked all requests from this device due to unusual activity"**

**Error:**
```
FirebaseAuthException: We have blocked all requests from this device due to unusual activity. Try again later.
```

**Reason:** Too many failed login attempts or Firebase detects emulator/testing.

**Solution:**
1. Wait 1-2 hours
2. **OR** Enable "Email link (passwordless sign-in)" in Firebase Console
3. **OR** Add SHA-1 fingerprint (see below)
4. **OR** Use different test accounts

---

### **Issue 2.2: "The email address is already in use"**

**Error:**
```
FirebaseAuthException: The email address is already in use by another account.
```

**Solution:**
- Use a different email address
- **OR** Delete the existing user:
  1. Firebase Console → Authentication → Users
  2. Find the user
  3. Click ⋮ (three dots) → Delete

---

### **Issue 2.3: "The password is invalid"**

**Error:**
```
FirebaseAuthException: The password is invalid or the user does not have a password.
```

**Solution:**
- Check password is correct
- Minimum 6 characters required
- Case-sensitive

---

### **Issue 2.4: "A network error has occurred"**

**Error:**
```
FirebaseAuthException: A network error has occurred.
```

**Solution:**
1. Check internet connection
2. Check Firebase project is active
3. Wait a few minutes and retry
4. Check Firebase Console for service status

---

## 3️⃣ FIRESTORE ISSUES

### **Issue 3.1: "Permission Denied" when reading/writing**

**Error:**
```
PERMISSION_DENIED: Missing or insufficient permissions.
```

**Reason:** Firestore security rules are too restrictive or not set up.

**Solution:**
Set up Firestore Rules in Firebase Console:

**Path:** Firestore Database → Rules

**For Development/Testing (INSECURE - DO NOT USE IN PRODUCTION):**
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if true;
    }
  }
}
```

**For Production (SECURE):**
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    
    // Users collection - users can read/write their own data
    match /users/{userId} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Reports collection
    match /reports/{reportId} {
      // Anyone authenticated can read all reports
      allow read: if request.auth != null;
      
      // Authenticated users can create reports
      allow create: if request.auth != null
                    && request.resource.data.createdByUid == request.auth.uid;
      
      // Only admin can update (for status changes)
      // Check if user email is in admin list
      allow update: if request.auth != null
                    && request.auth.token.email in [
                      'admin@gih.edu',
                      'admin@campus.edu'
                    ];
      
      // Users can delete their own reports, admins can delete any
      allow delete: if request.auth != null
                    && (request.auth.uid == resource.data.createdByUid
                        || request.auth.token.email in [
                          'admin@gih.edu',
                          'admin@campus.edu'
                        ]);
    }
    
    // Announcements collection
    match /announcements/{announcementId} {
      // Anyone authenticated can read announcements
      allow read: if request.auth != null;
      
      // Only admins can create/delete announcements
      allow create, delete: if request.auth != null
                             && request.auth.token.email in [
                               'admin@gih.edu',
                               'admin@campus.edu'
                             ];
    }
  }
}
```

**After updating rules:**
1. Click "Publish"
2. Wait 30 seconds
3. Try again in app

---

### **Issue 3.2: "Firestore is not initialized"**

**Error:**
```
FirebaseException: Firestore must be initialized before usage.
```

**Solution:**
1. Ensure `google-services.json` is in place
2. Sync Gradle
3. Clean and rebuild:
   ```
   Build → Clean Project
   Build → Rebuild Project
   ```

---

### **Issue 3.3: "Document does not exist"**

**Error:**
```
FirestoreException: Document doesn't exist.
```

**Solution:**
- Check collection and document names are correct
- Ensure data was actually written to Firestore
- Check Firestore Console to verify data exists

---

## 4️⃣ SHA FINGERPRINT ISSUES

### **Issue 4.1: "Google Play services not available"**

**Error:**
```
Google Play services are not available on this device.
```

**Reason:** Missing SHA-1 fingerprint in Firebase Console.

**Solution - Get SHA-1 Fingerprint:**

**Windows (PowerShell):**
```powershell
cd "C:\Users\Jeevandeep Saini\AndroidStudioProjects\GIH\CampusOne"
.\gradlew signingReport
```

**Look for output:**
```
Variant: debug
Config: debug
Store: C:\Users\...\.android\debug.keystore
Alias: AndroidDebugKey
MD5: XX:XX:XX...
SHA1: AA:BB:CC:DD:EE:FF:11:22:33:44:55:66:77:88:99:00:AA:BB:CC:DD
SHA-256: ...
```

**Copy the SHA1 value.**

---

### **Issue 4.2: Add SHA-1 to Firebase**

**Steps:**
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Select your project
3. Click ⚙️ (Settings) → Project settings
4. Scroll to "Your apps"
5. Click on your Android app
6. Scroll to "SHA certificate fingerprints"
7. Click "Add fingerprint"
8. Paste SHA-1 value
9. Click "Save"
10. Download new `google-services.json`
11. Replace old file in project
12. Sync Gradle
13. Rebuild app

---

## 5️⃣ GRADLE BUILD ISSUES

### **Issue 5.1: "Duplicate class found"**

**Error:**
```
Duplicate class com.google.android.gms...
```

**Solution:**
Check `app/build.gradle.kts` for duplicate Firebase dependencies.

**Remove duplicates:**
```kotlin
dependencies {
    // Keep only ONE of each
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")
    
    // Don't add these if using BOM:
    // implementation("com.google.firebase:firebase-auth:22.x.x") ❌
}
```

---

### **Issue 5.2: "Failed to resolve: com.google.firebase:firebase-..."**

**Error:**
```
Could not find com.google.firebase:firebase-auth:...
```

**Solution:**
1. Check internet connection
2. Add Google repository in `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        google()  // ← Must be here!
        mavenCentral()
    }
}
```

3. Sync Gradle
4. Invalidate caches:
   - File → Invalidate Caches → Invalidate and Restart

---

## 6️⃣ RUNTIME ISSUES

### **Issue 6.1: App crashes on startup**

**Error in Logcat:**
```
java.lang.IllegalStateException: Default FirebaseApp is not initialized
```

**Solution:**
1. Ensure `google-services.json` exists
2. Check `app/build.gradle.kts` has:
   ```kotlin
   plugins {
       id("com.google.gms.google-services")
   }
   ```
3. Clean and rebuild
4. Restart Android Studio

---

### **Issue 6.2: "FirebaseApp initialization unsuccessful"**

**Error:**
```
FirebaseApp initialization unsuccessful
```

**Solution:**
1. Check `google-services.json` is valid JSON
2. Check package name matches:
   - In `AndroidManifest.xml`: `package="com.gih.campusone"`
   - In `google-services.json`: `"package_name": "com.gih.campusone"`
3. Download fresh `google-services.json` from Firebase Console

---

## 7️⃣ EMULATOR-SPECIFIC ISSUES

### **Issue 7.1: "Unable to reach Firebase servers"**

**Solution:**
1. Ensure emulator has internet access
2. Wipe emulator data:
   - AVD Manager → ⋮ → Wipe Data
3. Restart emulator
4. Use emulator with Google Play

---

### **Issue 7.2: "Google Play services are updating"**

**Solution:**
1. Wait for update to complete
2. **OR** Use emulator with "Google APIs" (not "Google Play")
3. **OR** Use physical device

---

## 8️⃣ DATA NOT SYNCING

### **Issue 8.1: Reports/Announcements not appearing**

**Checklist:**
- [ ] Firestore rules allow read access
- [ ] User is authenticated
- [ ] Data exists in Firestore Console
- [ ] Using `collectAsStateWithLifecycle()` (not `collectAsState()`)
- [ ] ViewModel is not recreated on each recomposition

**Solution:**
Check Firestore Console → Data tab → Verify documents exist

---

### **Issue 8.2: Real-time updates not working**

**Solution:**
1. Check using `Flow` (not one-time fetch)
2. Verify Firestore listener is attached:
   ```kotlin
   // Correct - Real-time
   fun getReports() = callbackFlow<List<Report>> {
       // Firestore listener
   }
   
   // Wrong - One-time fetch
   suspend fun getReports(): List<Report> {
       // One-time fetch
   }
   ```
3. Check listener not being canceled

---

## 9️⃣ DEPLOYMENT ISSUES

### **Issue 9.1: App works in debug but not release**

**Reason:** Missing release SHA-1 fingerprint.

**Solution:**
1. Get release SHA-1:
   ```powershell
   keytool -list -v -keystore my-release-key.keystore
   ```
2. Add to Firebase Console (see Issue 4.2)
3. Download new `google-services.json`
4. Rebuild release APK

---

### **Issue 9.2: ProGuard issues with Firebase**

**Solution:**
Add to `proguard-rules.pro`:

```proguard
# Firebase
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }

# Firestore
-keepclassmembers class * {
    @com.google.firebase.firestore.PropertyName <methods>;
    @com.google.firebase.firestore.PropertyName <fields>;
}

# Keep data classes
-keep class com.gih.campusone.data.model.** { *; }
```

---

## 🔟 QUICK FIXES SUMMARY

### **App won't build:**
```powershell
# Clean and rebuild
.\gradlew clean
.\gradlew build --refresh-dependencies
```

### **Firebase not working:**
1. ✅ Check `google-services.json` exists in `app/` folder
2. ✅ Check plugin applied: `id("com.google.gms.google-services")`
3. ✅ Sync Gradle
4. ✅ Clean and rebuild

### **Permission denied:**
1. ✅ Update Firestore rules (see Issue 3.1)
2. ✅ Publish rules
3. ✅ Wait 30 seconds
4. ✅ Try again

### **SHA fingerprint:**
```powershell
# Get SHA-1
.\gradlew signingReport

# Copy SHA1 value
# Add to Firebase Console
# Download new google-services.json
# Replace old file
# Sync Gradle
```

---

## 📚 HELPFUL LINKS

- **Firebase Console:** https://console.firebase.google.com/
- **Firebase Android Setup:** https://firebase.google.com/docs/android/setup
- **Firebase Auth Docs:** https://firebase.google.com/docs/auth/android/start
- **Firestore Docs:** https://firebase.google.com/docs/firestore/quickstart
- **Security Rules:** https://firebase.google.com/docs/firestore/security/get-started

---

## 🆘 EMERGENCY CHECKLIST

**If nothing works:**

1. [ ] Delete `app/google-services.json`
2. [ ] Go to Firebase Console
3. [ ] Delete Android app from project
4. [ ] Re-add Android app with package name: `com.gih.campusone`
5. [ ] Download new `google-services.json`
6. [ ] Place in `app/` folder
7. [ ] Get SHA-1: `.\gradlew signingReport`
8. [ ] Add SHA-1 to Firebase Console
9. [ ] Download updated `google-services.json`
10. [ ] Replace file
11. [ ] File → Invalidate Caches → Invalidate and Restart
12. [ ] Clean project: Build → Clean Project
13. [ ] Rebuild: Build → Rebuild Project
14. [ ] Run app

---

## 📞 DEBUGGING TIPS

### **Enable Firebase Debug Logging:**

```kotlin
// In MainActivity.onCreate()
FirebaseFirestore.setLoggingEnabled(true)
```

### **Check Logcat:**

Filter by: `Firebase`

Look for:
- `FirebaseAuth` - Authentication logs
- `Firestore` - Database logs
- `GoogleApiAvailability` - Google Play Services logs

---

**🎉 With these fixes, your Firebase integration should work perfectly! Most issues are solved by ensuring `google-services.json` is in the right place and Firestore rules are set up correctly.**

