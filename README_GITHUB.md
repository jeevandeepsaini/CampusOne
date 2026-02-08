# 🎓 CampusOne - Smart Campus Solution

> A comprehensive mobile application designed to streamline campus operations, enhance communication, and improve the overall campus experience.

[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://www.android.com/)
[![Language](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org/)
[![Framework](https://img.shields.io/badge/Framework-Jetpack%20Compose-orange.svg)](https://developer.android.com/jetpack/compose)
[![Firebase](https://img.shields.io/badge/Backend-Firebase-yellow.svg)](https://firebase.google.com/)

**Submitted for:** Great Indian Hackathon 2026  
**Developer:** Jeevandeep Saini (GIH020JEE)  
**Institution:** The NorthCap University

---

## 📱 About

CampusOne is a modern Android application built with Jetpack Compose that solves real campus problems through digital solutions. The app provides role-based access for Students, Professors, and Administrators with features tailored to each user type.

---

## ✨ Key Features

### 🔐 **Authentication & User Management**
- Firebase Email/Password authentication
- Role-based access control (Student/Professor/Admin)
- Secure user management with Firestore

### 🚨 **Emergency SOS**
- Quick access to campus emergency contacts (Security, Medical, Warden)
- One-tap direct dial functionality
- Campus location on Google Maps
- National emergency numbers (100, 101, 102, 112)

### 📝 **Issue Reporting System**
- **Students/Professors:**
  - Report campus problems (Infrastructure, Hygiene, Security, Network, Other)
  - Track personal report history
  - View report status in real-time
  - Optional location field
- **Admins:**
  - View ALL campus reports
  - Update status (Pending → In Progress → Resolved)
  - Filter and manage reports efficiently
  - Real-time statistics dashboard

### 📢 **Announcements System**
- **All Users:** View campus-wide announcements
- **Admins:** Create and delete announcements
- Real-time updates across all devices
- Clean, card-based UI

### 👨‍💼 **Admin Dashboard**
- Comprehensive admin interface
- Quick access to all management features
- Report statistics (Pending, In Progress, Resolved)
- Professional Material3 design

### ℹ️ **About & Information**
- App information and version
- Developer profile with photo
- Feature overview
- Institution details

---

## 🛠️ Tech Stack

| Category | Technologies |
|----------|-------------|
| **Language** | Kotlin |
| **UI Framework** | Jetpack Compose |
| **Design System** | Material Design 3 |
| **Navigation** | Navigation Compose |
| **Architecture** | MVVM + Repository Pattern |
| **Async** | Kotlin Coroutines & Flow |
| **State Management** | StateFlow, collectAsStateWithLifecycle |
| **Backend** | Firebase Authentication, Cloud Firestore |
| **Dependency Injection** | ViewModel Factory |

---

## 📂 Project Structure

```
com.gih.campusone/
├── data/
│   ├── model/              # Data classes (User, Report, Announcement)
│   ├── repository/         # Repository layer
│   │   ├── AuthRepository
│   │   ├── UserRepository
│   │   ├── ReportRepository
│   │   └── AnnouncementRepository
│   └── Result.kt           # Sealed class for operation results
│
├── ui/
│   ├── navigation/         # Navigation setup
│   │   ├── Routes.kt       # Route definitions
│   │   └── AppNavigation.kt
│   │
│   ├── screens/
│   │   ├── auth/           # LoginScreen, SignUpScreen
│   │   ├── home/           # HomeScreen
│   │   ├── sos/            # SosScreen (Emergency contacts)
│   │   ├── reports/        # CreateReport, MyReports, ReportDetail
│   │   ├── announcements/  # Announcements, AnnouncementDetail
│   │   ├── admin/          # AdminHome, AllReports, ManageAnnouncements
│   │   └── about/          # AboutScreen
│   │
│   └── theme/              # Material3 theme (Color, Type, Theme)
│
├── utils/                  # Extensions, Constants
│   ├── Constants.kt        # Admin emails, collection names
│   └── Extensions.kt       # Date formatting, etc.
│
└── MainActivity.kt         # App entry point
```

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17 or higher
- Android SDK (API 24 - Android 7.0 minimum)
- Firebase account

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/CampusOne.git
   cd CampusOne
   ```

2. **Firebase Setup** (IMPORTANT!)
   
   a. Create Firebase Project:
   - Go to [Firebase Console](https://console.firebase.google.com/)
   - Create project: "CampusOne"
   
   b. Add Android App:
   - Package name: `com.gih.campusone`
   - Download `google-services.json`
   - Place in: `app/google-services.json`
   
   c. Enable Services:
   - **Authentication:** Enable Email/Password
   - **Firestore Database:** Create database
   
   d. Configure Security Rules (see below)
   
   e. Create Indexes (see below)

3. **Sync and Build**
   ```bash
   ./gradlew build
   ```

4. **Run**
   - Open in Android Studio
   - Sync Gradle
   - Run on emulator or device

---

## 🔒 Firebase Configuration

### Security Rules

Go to Firestore → Rules and paste:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    
    // Users collection
    match /users/{userId} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Reports collection
    match /reports/{reportId} {
      allow read: if request.auth != null;
      allow create: if request.auth != null && 
                    request.resource.data.createdByUid == request.auth.uid;
      allow update: if request.auth != null && 
                    request.auth.token.email in ['admin@gih.edu', 'admin@campus.edu'];
      allow delete: if request.auth != null && 
                    (request.auth.uid == resource.data.createdByUid || 
                     request.auth.token.email in ['admin@gih.edu', 'admin@campus.edu']);
    }
    
    // Announcements collection
    match /announcements/{announcementId} {
      allow read: if request.auth != null;
      allow create, delete: if request.auth != null && 
                             request.auth.token.email in ['admin@gih.edu', 'admin@campus.edu'];
    }
  }
}
```

### Required Indexes

Go to Firestore → Indexes → Create Index:

**Index 1 - My Reports:**
- Collection ID: `reports`
- Fields:
  - `createdByUid` (Ascending)
  - `createdAt` (Descending)

**Index 2 - All Reports (Optional):**
- Collection ID: `reports`
- Fields:
  - `status` (Ascending)
  - `createdAt` (Descending)

---

## 👥 User Roles & Access

| Feature | Student | Professor | Admin |
|---------|---------|-----------|-------|
| Login/Signup | ✅ | ✅ | ✅ |
| Emergency SOS | ✅ | ✅ | ✅ |
| Create Reports | ✅ | ✅ | ✅ |
| View My Reports | ✅ | ✅ | ✅ |
| View Announcements | ✅ | ✅ | ✅ |
| View All Reports | ❌ | ❌ | ✅ |
| Update Report Status | ❌ | ❌ | ✅ |
| Create Announcements | ❌ | ❌ | ✅ |
| Delete Announcements | ❌ | ❌ | ✅ |
| Admin Dashboard | ❌ | ❌ | ✅ |

**Admin Emails (Hardcoded in Constants.kt):**
- `admin@gih.edu`
- `admin@campus.edu`
- `campusone.admin@gmail.com`

---

## 🧪 Testing

### Test Accounts

Create these in Firebase Authentication for testing:

```
Student:
  Email: student@test.com
  Password: test123

Professor:
  Email: professor@test.com
  Password: test123

Admin:
  Email: admin@gih.edu
  Password: admin123
```

### Quick Test Flow

1. ✅ Sign up as student
2. ✅ Create a report
3. ✅ View My Reports
4. ✅ View Announcements
5. ✅ Logout
6. ✅ Login as admin
7. ✅ View All Reports
8. ✅ Update report status
9. ✅ Create announcement
10. ✅ Delete announcement

---

## 📊 Database Schema

### Collections Structure

```
users/{uid}
  ├── email: string
  ├── role: string (student/professor)
  └── createdAt: Timestamp

reports/{reportId}
  ├── category: string
  ├── description: string
  ├── location: string (optional)
  ├── status: string (Pending/In Progress/Resolved)
  ├── createdAt: Timestamp
  ├── updatedAt: Timestamp
  ├── createdByUid: string
  ├── createdByEmail: string
  └── createdByRole: string

announcements/{announcementId}
  ├── title: string
  ├── message: string
  ├── createdAt: Timestamp
  └── createdByUid: string
```

---

## 🐛 Troubleshooting

### Common Issues

**Issue:** "google-services.json missing"
```
Solution: Download from Firebase Console → Project Settings → Your apps
Place in: app/google-services.json
```

**Issue:** "Firestore index required"
```
Solution: Click the link in the error message or create manually in Firebase Console
Wait 2-5 minutes for index to build
```

**Issue:** "Permission denied"
```
Solution: Check Firestore Security Rules are configured correctly
Ensure user is authenticated
```

**Issue:** "Admin features not showing"
```
Solution: Verify email is in ADMIN_EMAILS list in Constants.kt
Check Firebase user email matches exactly
```

---

## 🤝 Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project was created as part of the Great Indian Hackathon 2026 submission.

© 2026 CampusOne. All rights reserved.

---

## 👨‍💻 Developer

**Jeevandeep Saini**  
📧 Email: [Your Email]  
🎓 GIH ID: GIH020JEE  
🏫 Institution: The NorthCap University  
💼 LinkedIn: [Your LinkedIn]  
🐙 GitHub: [Your GitHub]

---

## 🙏 Acknowledgments

- Firebase for backend infrastructure
- Material Design 3 for beautiful components
- Jetpack Compose team for modern UI toolkit
- Great Indian Hackathon organizers
- The NorthCap University for support

---

## 📞 Support

Have questions or issues?
- 🐛 [Create an Issue](https://github.com/yourusername/CampusOne/issues)
- 📧 Email: [Your Email]
- 💬 Discussion: [GitHub Discussions](https://github.com/yourusername/CampusOne/discussions)

---

## ⭐ Show Your Support

If you like this project, please give it a ⭐ on GitHub!

---

**Built with ❤️ for the Great Indian Hackathon 2026**

*Making campuses smarter, one feature at a time!* 🎓✨

