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
- **Admins:**
  - View ALL campus reports
  - Update status (Pending → In Progress → Resolved)
  - Real-time statistics dashboard

### 📢 **Announcements System**
- View campus-wide announcements
- Admin controls for creation/deletion
- Real-time updates across all devices

### 👨‍💼 **Admin Dashboard**
- Comprehensive admin interface
- Report management and statistics
- Professional Material3 design

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

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17 or higher
- Android SDK (API 24 minimum)
- Firebase account

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/jeevandeepsaini/CampusOne.git
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

3. **Build and Run**
   ```bash
   ./gradlew build
   ```

---

## 🔒 Firebase Configuration

### Security Rules

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /users/{userId} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && request.auth.uid == userId;
    }
    
    match /reports/{reportId} {
      allow read: if request.auth != null;
      allow create: if request.auth != null;
      allow update: if request.auth != null && 
                    request.auth.token.email in ['admin@gih.edu', 'admin@campus.edu'];
    }
    
    match /announcements/{announcementId} {
      allow read: if request.auth != null;
      allow create, delete: if request.auth != null && 
                             request.auth.token.email in ['admin@gih.edu', 'admin@campus.edu'];
    }
  }
}
```

### Required Indexes

**Index 1 - My Reports:**
- Collection ID: `reports`
- Fields: `createdByUid` (Ascending), `createdAt` (Descending)

**Index 2 - All Reports:**
- Collection ID: `reports`
- Fields: `status` (Ascending), `createdAt` (Descending)

---

## 👥 User Roles

| Feature | Student | Professor | Admin |
|---------|---------|-----------|-------|
| Emergency SOS | ✅ | ✅ | ✅ |
| Create Reports | ✅ | ✅ | ✅ |
| View My Reports | ✅ | ✅ | ✅ |
| View Announcements | ✅ | ✅ | ✅ |
| View All Reports | ❌ | ❌ | ✅ |
| Update Report Status | ❌ | ❌ | ✅ |
| Create/Delete Announcements | ❌ | ❌ | ✅ |

**Admin Emails:** `admin@gih.edu`, `admin@campus.edu`

---

## 🧪 Testing

### Test Accounts
```
Student: student@test.com / test123
Admin: admin@gih.edu / admin123
```

---

## 📊 Database Schema

```
users/{uid}
  ├── email: string
  ├── role: string
  └── createdAt: Timestamp

reports/{reportId}
  ├── category: string
  ├── description: string
  ├── status: string
  ├── createdAt: Timestamp
  └── createdByUid: string

announcements/{announcementId}
  ├── title: string
  ├── message: string
  └── createdAt: Timestamp
```

---

## 🐛 Troubleshooting

**"google-services.json missing"**
- Download from Firebase Console → Project Settings
- Place in `app/` directory

**"Firestore index required"**
- Click the error link to create index
- Wait 2-5 minutes for build

**"Permission denied"**
- Check Firestore Security Rules
- Ensure user is authenticated

---

## 📄 License

This project was created as part of the Great Indian Hackathon 2026 submission.

© 2026 CampusOne. All rights reserved.

---

## 👨‍💻 Developer

**Jeevandeep Saini**  
🎓 GIH ID: GIH020JEE  
🏫 Institution: The NorthCap University  
🐙 GitHub: [@jeevandeepsaini](https://github.com/jeevandeepsaini)

---

## 🙏 Acknowledgments

- Firebase for backend infrastructure
- Material Design 3 for UI components
- Jetpack Compose team
- Great Indian Hackathon organizers
- The NorthCap University

---

**Built with ❤️ for the Great Indian Hackathon 2026**

*Making campuses smarter, one feature at a time!* 🎓✨

