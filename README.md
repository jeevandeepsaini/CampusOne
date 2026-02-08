# 🚀 CampusOne - Quick Setup Checklist

## ✅ Step 1: Gradle Configuration (COMPLETED ✓)

All Gradle files have been updated with:
- ✅ Firebase BOM 33.7.0
- ✅ Firebase Authentication
- ✅ Firebase Firestore  
- ✅ Navigation Compose 2.8.5
- ✅ ViewModel Compose 2.10.0
- ✅ Coroutines 1.9.0
- ✅ Google Services Plugin 4.4.2

---

## 📋 Step 2: Firebase Console Setup (DO THIS NOW)

### Quick Steps:

1. **Go to:** https://console.firebase.google.com/

2. **Create/Select Project:** "CampusOne"

3. **Add Android App:**
   - Package name: `com.gih.campusone`
   - Download `google-services.json`
   - **Place file here:** `CampusOne/app/google-services.json`

4. **Enable Authentication:**
   - Go to: Build → Authentication → Get Started
   - Enable: Email/Password sign-in method

5. **Enable Firestore:**
   - Go to: Build → Firestore Database → Create Database
   - Start in: Test Mode (for development)
   - Choose: Closest location

---

## 🔄 Step 3: Sync & Build

In Android Studio:

```
1. Place google-services.json in app/ folder
2. Click "Sync Now" banner
3. Wait for sync to complete
4. Build → Rebuild Project
```

**Expected Gradle Sync Time:** 2-5 minutes (first time)

---

## ✅ Step 4: Verify Setup

Check these in Android Studio:

- [ ] No red errors in Gradle files
- [ ] "Build successful" message in Build tab
- [ ] `google-services.json` visible in app folder
- [ ] Firebase Authentication enabled in console
- [ ] Firestore database created in console

---

## 🎨 Step 5: Generate Code (TYPE "go")

Once Steps 2-4 are complete, type **"go"** and I'll generate:

### Data Layer (Models & Repositories):
- `data/model/User.kt`
- `data/model/Report.kt`
- `data/model/Announcement.kt`
- `data/Result.kt`
- `data/repository/AuthRepository.kt`
- `data/repository/ReportRepository.kt`
- `data/repository/AnnouncementRepository.kt`

### UI Layer (Screens & ViewModels):
- `ui/navigation/NavRoutes.kt`
- `ui/navigation/AppNavigation.kt`
- `ui/screens/auth/LoginScreen.kt`
- `ui/screens/auth/SignUpScreen.kt`
- `ui/screens/auth/AuthViewModel.kt`
- `ui/screens/home/HomeScreen.kt`
- `ui/screens/home/HomeViewModel.kt`
- `ui/screens/sos/EmergencySOSScreen.kt`
- `ui/screens/reports/ReportIssueScreen.kt`
- `ui/screens/reports/MyReportsScreen.kt`
- `ui/screens/reports/AllReportsScreen.kt`
- `ui/screens/reports/ReportsViewModel.kt`
- `ui/screens/announcements/AnnouncementsScreen.kt`
- `ui/screens/announcements/CreateAnnouncementScreen.kt`
- `ui/screens/announcements/AnnouncementsViewModel.kt`

### Utilities:
- `utils/Constants.kt`
- `utils/Extensions.kt`
- `ui/components/LoadingDialog.kt`
- `ui/components/ErrorDialog.kt`

### Updated Theme:
- `ui/theme/Color.kt` (modern vibrant colors)

### MainActivity:
- Updated `MainActivity.kt` with navigation setup

---

## 📁 File Locations

```
CampusOne/
├── app/
│   ├── google-services.json  ← MUST BE HERE!
│   ├── build.gradle.kts      ← Updated ✓
│   └── src/main/java/com/gih/campusone/
│       ├── MainActivity.kt
│       ├── data/...           ← Will be generated
│       ├── ui/...             ← Will be generated
│       └── utils/...          ← Will be generated
├── build.gradle.kts           ← Updated ✓
├── gradle/
│   └── libs.versions.toml     ← Updated ✓
├── FIREBASE_SETUP_GUIDE.md    ← Created ✓
├── GRADLE_CHANGES.md          ← Created ✓
└── README.md                  ← This file

```

---

## 🔐 Admin Configuration

Default admin emails (will be in Constants.kt):
```kotlin
val ADMIN_EMAILS = setOf(
    "admin@campusone.com",
    "admin@gih.edu"
)
```

**To add more admins:** Update this set in the generated code.

---

## 🎯 App Features to be Generated

1. **Authentication** (Login/SignUp with role selection)
2. **Home Dashboard** (Quick access cards)
3. **Emergency SOS** (Contact list with call/email actions)
4. **Report Issue** (Submit issues with categories)
5. **My Reports** (User's report history with status)
6. **All Reports** (Admin view with status updates)
7. **Announcements** (View for all, create for admin)
8. **Real-time Updates** (Firestore Flow integration)

---

## ⚡ Quick Commands

### Gradle Sync:
```powershell
cd "C:\Users\Jeevandeep Saini\AndroidStudioProjects\GIH\CampusOne"
.\gradlew --refresh-dependencies
```

### Clean Build:
```powershell
.\gradlew clean build
```

### Check Dependencies:
```powershell
.\gradlew app:dependencies
```

---

## 🐛 Troubleshooting

**Issue:** "google-services.json not found"
- **Fix:** Ensure file is in `app/` folder, then sync Gradle

**Issue:** Firebase initialization fails
- **Fix:** Check package name in `google-services.json` matches `com.gih.campusone`

**Issue:** Gradle sync errors
- **Fix:** File → Invalidate Caches → Restart

---

## 📞 Documentation Links

- [Firebase Console](https://console.firebase.google.com/)
- [Firebase Auth Docs](https://firebase.google.com/docs/auth/android/start)
- [Firestore Docs](https://firebase.google.com/docs/firestore/quickstart)
- [Compose Navigation](https://developer.android.com/jetpack/compose/navigation)

---

## ✨ Ready to Code?

**Current Status:** Gradle configured ✓

**Next:** Complete Firebase setup (Steps 2-4), then type **"go"**

**Estimated Setup Time:** 10-15 minutes
**Estimated Code Generation Time:** 2-3 minutes

---

🎉 **Let's build CampusOne!**


