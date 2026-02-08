# 🔐 AuthViewModel & Auth State - Generation Complete!

## ✅ Files Created: 3

### Auth State Management (3 files):
✅ `ui/screens/auth/AuthState.kt` - Sealed class for auth state
✅ `ui/screens/auth/AuthViewModel.kt` - ViewModel with auth logic
✅ `ui/screens/auth/AuthUiState.kt` - UI state for auth screens

---

## 📁 Updated Project Structure

```
com.gih.campusone/
├── data/
│   ├── model/              ✅ (3 files)
│   ├── repository/         ✅ (4 files)
│   └── Result.kt           ✅
│
├── ui/
│   ├── screens/
│   │   └── auth/
│   │       ├── AuthState.kt       ✅ NEW
│   │       ├── AuthViewModel.kt   ✅ NEW
│   │       └── AuthUiState.kt     ✅ NEW
│   └── theme/              (existing)
│
├── utils/
│   ├── Constants.kt        ✅ (ADMIN_EMAILS already present)
│   └── Extensions.kt       ✅
│
└── MainActivity.kt         (existing)
```

**Total Files Generated: 13**

---

## 🎯 AuthState - Sealed Class

### **Definition:**
```kotlin
sealed class AuthState {
    object Loading : AuthState()
    object Unauthenticated : AuthState()
    data class Authenticated(
        val uid: String,
        val email: String,
        val role: String,
        val isAdmin: Boolean
    ) : AuthState()
}
```

### **States:**
1. **Loading** - Checking auth or performing operations
2. **Unauthenticated** - User is logged out
3. **Authenticated** - User is logged in with:
   - `uid` - Firebase user ID
   - `email` - User email
   - `role` - User role (student/professor/admin)
   - `isAdmin` - Computed from ADMIN_EMAILS

---

## 🧠 AuthViewModel - Features

### **Exposed State:**
```kotlin
val authState: StateFlow<AuthState>
val errorMessage: StateFlow<String?>
```

### **Methods:**

#### **1. Auto-Check on App Start (`init` block):**
```kotlin
init {
    checkCurrentUser()
}
```
- ✅ Checks if `FirebaseAuth.currentUser` exists
- ✅ If exists → fetch role from Firestore `users/{uid}`
- ✅ Computes `isAdmin` using `email in Constants.ADMIN_EMAILS`
- ✅ Sets state to `Authenticated` or `Unauthenticated`

#### **2. Login:**
```kotlin
fun login(email: String, password: String)
```
**Flow:**
1. Sets state to `Loading`
2. Calls `authRepository.login(email, password)`
3. On success → fetches role from Firestore
4. Computes `isAdmin` from email
5. Sets state to `Authenticated(uid, email, role, isAdmin)`
6. On error → sets state to `Unauthenticated` with error message

#### **3. Signup:**
```kotlin
fun signup(email: String, password: String, role: String, name: String = "")
```
**Flow:**
1. Sets state to `Loading`
2. Calls `authRepository.signUp(email, password, role, name)`
3. Creates Firebase Auth user
4. Creates Firestore document: `users/{uid}` with:
   - uid, email, role, name, createdAt (Timestamp)
5. Computes `isAdmin` from email
6. Sets state to `Authenticated(uid, email, role, isAdmin)`
7. On error → sets state to `Unauthenticated` with error message

#### **4. Logout:**
```kotlin
fun logout()
```
**Flow:**
1. Sets state to `Loading`
2. Calls `authRepository.logout()`
3. Sets state to `Unauthenticated`
4. Clears error message

#### **5. Helper Methods:**
```kotlin
fun clearError()                  // Clear error message
fun reloadUser()                  // Reload current user data
fun isAdmin(): Boolean            // Check if current user is admin
fun getCurrentUserId(): String?   // Get current user ID
fun getCurrentUserEmail(): String?// Get current user email
fun getCurrentUserRole(): String? // Get current user role
```

---

## 🔐 Admin Detection Logic

### **How it Works:**

```kotlin
val isAdmin = email in Constants.ADMIN_EMAILS
```

### **Admin Emails (in Constants.kt):**
```kotlin
val ADMIN_EMAILS = setOf(
    "admin@campusone.com",
    "admin@gih.edu",
    "campusone.admin@gmail.com"
)
```

### **When Admin is Detected:**
- During **signup**: Immediately after user creation
- During **login**: After fetching role from Firestore
- On **app start**: When checking current user

### **Admin Privileges:**
- Can view **All Reports** (not just their own)
- Can **update report status** (Pending → In Progress → Resolved)
- Can **create announcements**
- Can **delete announcements**

---

## 🔄 State Flow Diagram

```
App Start
    ↓
[Loading]
    ↓
Check FirebaseAuth.currentUser
    ↓
    ├─→ null → [Unauthenticated]
    │
    └─→ exists → Fetch role from Firestore users/{uid}
                      ↓
                  Compute isAdmin (email in ADMIN_EMAILS)
                      ↓
                  [Authenticated(uid, email, role, isAdmin)]

Login/Signup
    ↓
[Loading]
    ↓
Firebase Auth Operation
    ↓
    ├─→ Success → Fetch/Create user doc → [Authenticated]
    └─→ Error → [Unauthenticated] + error message

Logout
    ↓
[Loading]
    ↓
Firebase Auth signOut()
    ↓
[Unauthenticated]
```

---

## 💡 Usage Example (Future UI Screens)

### **Collecting Auth State in Composable:**
```kotlin
@Composable
fun MyScreen(authViewModel: AuthViewModel = viewModel()) {
    val authState by authViewModel.authState.collectAsState()
    val errorMessage by authViewModel.errorMessage.collectAsState()
    
    when (authState) {
        is AuthState.Loading -> {
            LoadingScreen()
        }
        is AuthState.Unauthenticated -> {
            LoginScreen(
                onLogin = { email, password ->
                    authViewModel.login(email, password)
                }
            )
        }
        is AuthState.Authenticated -> {
            val state = authState as AuthState.Authenticated
            if (state.isAdmin) {
                AdminHomeScreen()
            } else {
                UserHomeScreen()
            }
        }
    }
    
    errorMessage?.let { error ->
        ErrorDialog(
            message = error,
            onDismiss = { authViewModel.clearError() }
        )
    }
}
```

### **Login Example:**
```kotlin
authViewModel.login(
    email = "student@campus.edu",
    password = "password123"
)
// State will change:
// Loading → Authenticated(uid, email, "student", isAdmin=false)
```

### **Signup Example:**
```kotlin
authViewModel.signup(
    email = "john@campus.edu",
    password = "securepass",
    role = "student",
    name = "John Doe"
)
// Creates auth user + Firestore doc
// State will change:
// Loading → Authenticated(uid, email, "student", isAdmin=false)
```

### **Admin Check:**
```kotlin
val isAdmin = authViewModel.isAdmin()
if (isAdmin) {
    // Show admin menu items
    NavigateToAllReports()
    NavigateToCreateAnnouncement()
} else {
    // Show regular user menu
    NavigateToMyReports()
}
```

---

## 🗄️ Firestore Document Creation

### **On Signup, creates:**
```
users/{uid}/
├── uid: "abc123"
├── email: "student@campus.edu"
├── role: "student"
├── name: "John Doe"
└── createdAt: Timestamp(now)
```

### **On Login, reads:**
```
users/{uid}/ → get role
↓
Compute isAdmin: email in ADMIN_EMAILS
↓
AuthState.Authenticated(uid, email, role, isAdmin)
```

---

## ⚡ Key Features

### ✅ **Auto-State Check on App Start**
- No need to manually check auth state
- ViewModel automatically checks on initialization
- Handles case where user is already logged in

### ✅ **Admin Detection**
- Uses hardcoded `ADMIN_EMAILS` set
- Computed on every login/signup
- No separate admin flag in Firestore needed

### ✅ **Error Handling**
- All operations wrapped in try-catch
- Error messages exposed via `errorMessage` StateFlow
- Can be displayed in UI and cleared

### ✅ **Edge Case Handling**
- Handles missing Firestore user doc (creates default)
- Handles null email gracefully
- Handles auth state changes

### ✅ **Type-Safe State**
- Uses sealed class for compile-time safety
- Can't access uid/email/role unless in Authenticated state
- Exhaustive when expressions

---

## 🧪 Testing Scenarios

### **Scenario 1: New User Signup**
```
1. User enters: email="test@campus.edu", password="test123", role="student"
2. authViewModel.signup(...)
3. Firebase Auth creates user
4. Firestore creates users/{uid} doc
5. isAdmin = "test@campus.edu" in ADMIN_EMAILS = false
6. authState = Authenticated(uid, "test@campus.edu", "student", false)
```

### **Scenario 2: Admin Signup**
```
1. User enters: email="admin@gih.edu", password="admin123", role="professor"
2. authViewModel.signup(...)
3. Firebase Auth creates user
4. Firestore creates users/{uid} doc
5. isAdmin = "admin@gih.edu" in ADMIN_EMAILS = true ✓
6. authState = Authenticated(uid, "admin@gih.edu", "professor", true)
```

### **Scenario 3: App Restart (User Already Logged In)**
```
1. App starts
2. AuthViewModel init block runs
3. checkCurrentUser() finds FirebaseAuth.currentUser != null
4. Fetches role from Firestore users/{uid}
5. Computes isAdmin from email
6. authState = Authenticated(uid, email, role, isAdmin)
7. User stays logged in, no login screen shown
```

### **Scenario 4: Login with Wrong Password**
```
1. authViewModel.login("test@campus.edu", "wrongpass")
2. Firebase Auth returns error
3. authState = Unauthenticated
4. errorMessage = "The password is invalid or the user does not have a password."
5. UI shows error dialog
```

---

## 🎨 Next Steps: UI Screens

Ready to generate:

### **Auth Screens:**
- ✅ LoginScreen.kt - Email/password login UI
- ✅ SignUpScreen.kt - Signup with role selection UI

### **Navigation:**
- ✅ NavRoutes.kt - Route definitions
- ✅ AppNavigation.kt - NavHost with auth state handling

### **Home Screens:**
- ✅ HomeScreen.kt - Dashboard (different for admin vs user)
- ✅ HomeViewModel.kt - Home screen logic

### **Other Screens:**
- Emergency SOS
- Reports (Create, My Reports, All Reports)
- Announcements (View, Create)
- Components (Dialogs, Buttons)

---

## ✅ Validation

**All checks passed:**
- ✅ No compile errors
- ✅ AuthState sealed class with 3 states
- ✅ AuthViewModel with StateFlow
- ✅ Login, signup, logout implemented
- ✅ Auto-check on app start
- ✅ Admin detection via ADMIN_EMAILS
- ✅ Firestore role fetching
- ✅ Error handling with Result<T>
- ✅ Helper methods for role/admin checks
- ⚠️ Only "unused" warnings (expected)

---

## 📚 Files Summary

| File | Lines | Purpose |
|------|-------|---------|
| AuthState.kt | 24 | Sealed class for auth states |
| AuthViewModel.kt | 225 | Auth logic + state management |
| AuthUiState.kt | 14 | UI state for auth screens |

**Total: 3 files, ~263 lines**

---

✅ **AuthViewModel complete! Ready for UI screen generation.**

Type **"next"** or **"continue"** to generate Login, SignUp, and Navigation screens.


