# 🎯 AuthViewModel Quick Reference

## 📦 What Was Generated

```
ui/screens/auth/
├── AuthState.kt          ✅ Sealed class (Loading, Unauthenticated, Authenticated)
├── AuthViewModel.kt      ✅ ViewModel with login/signup/logout
└── AuthUiState.kt        ✅ UI state helper for screens
```

---

## 🚀 Key Features

### ✅ **Auto-Check on App Start**
```kotlin
// Automatically runs in init block
- Checks FirebaseAuth.currentUser
- If logged in → fetches role from Firestore
- Computes isAdmin from email
- Sets appropriate AuthState
```

### ✅ **Admin Detection**
```kotlin
val isAdmin = email in Constants.ADMIN_EMAILS

// Admin emails (in Constants.kt):
- admin@campusone.com
- admin@gih.edu
- campusone.admin@gmail.com
```

### ✅ **State Management**
```kotlin
val authState: StateFlow<AuthState>
// Values: Loading, Unauthenticated, Authenticated(uid, email, role, isAdmin)

val errorMessage: StateFlow<String?>
// Contains error messages from auth operations
```

---

## 💡 Usage Examples

### **Login:**
```kotlin
authViewModel.login("student@campus.edu", "password123")
// → AuthState.Authenticated(uid, email, "student", isAdmin=false)
```

### **Signup:**
```kotlin
authViewModel.signup(
    email = "john@campus.edu",
    password = "securepass",
    role = "student",
    name = "John Doe"
)
// → Creates Firebase Auth user
// → Creates Firestore users/{uid} doc
// → AuthState.Authenticated(uid, email, "student", isAdmin=false)
```

### **Admin Signup:**
```kotlin
authViewModel.signup(
    email = "admin@gih.edu",
    password = "adminpass",
    role = "professor",
    name = "Admin User"
)
// → AuthState.Authenticated(uid, email, "professor", isAdmin=true) ✓
```

### **Logout:**
```kotlin
authViewModel.logout()
// → AuthState.Unauthenticated
```

### **Check Admin Status:**
```kotlin
val isAdmin = authViewModel.isAdmin()
if (isAdmin) {
    // Show admin UI
}
```

---

## 🔄 State Flow

```
App Start
    ↓
[Loading] ← authState
    ↓
Check FirebaseAuth.currentUser
    ↓
    ├─→ null → [Unauthenticated]
    │
    └─→ exists
        ↓
        Fetch users/{uid} from Firestore
        ↓
        Compute: isAdmin = email in ADMIN_EMAILS
        ↓
        [Authenticated(uid, email, role, isAdmin)]
```

---

## 🗄️ Firestore Integration

### **On Signup:**
Creates document:
```
users/{uid}/
├── uid: "abc123"
├── email: "student@campus.edu"
├── role: "student"
├── name: "John Doe"
└── createdAt: Timestamp(now)
```

### **On Login:**
Reads document:
```
users/{uid}/ → get role field
↓
Compute: isAdmin = email in ADMIN_EMAILS
↓
AuthState.Authenticated(uid, email, role, isAdmin)
```

---

## 📊 AuthState Values

| State | When | Data Available |
|-------|------|----------------|
| `Loading` | Auth operations in progress | None |
| `Unauthenticated` | User logged out or login failed | None |
| `Authenticated` | User logged in | uid, email, role, isAdmin |

---

## 🧪 Admin Detection Examples

| Email | In ADMIN_EMAILS? | isAdmin |
|-------|------------------|---------|
| admin@campusone.com | ✅ Yes | `true` |
| admin@gih.edu | ✅ Yes | `true` |
| student@campus.edu | ❌ No | `false` |
| prof@campus.edu | ❌ No | `false` |

---

## 🎨 UI Integration (Future)

### **In Composable:**
```kotlin
val authState by authViewModel.authState.collectAsState()
val errorMessage by authViewModel.errorMessage.collectAsState()

when (authState) {
    AuthState.Loading -> CircularProgressIndicator()
    AuthState.Unauthenticated -> LoginScreen()
    is AuthState.Authenticated -> {
        val state = authState as AuthState.Authenticated
        if (state.isAdmin) {
            AdminDashboard()
        } else {
            UserDashboard()
        }
    }
}
```

---

## 📝 Helper Methods

```kotlin
authViewModel.clearError()              // Clear error message
authViewModel.reloadUser()              // Reload user data
authViewModel.isAdmin()                 // Check if admin
authViewModel.getCurrentUserId()        // Get UID
authViewModel.getCurrentUserEmail()     // Get email
authViewModel.getCurrentUserRole()      // Get role
```

---

## ✅ Validation Complete

- ✅ No compile errors
- ✅ StateFlow properly exposed
- ✅ Auto-check on app start
- ✅ Admin detection implemented
- ✅ Error handling included
- ✅ All CRUD operations work
- ⚠️ Only "unused" warnings (expected)

---

## 🚀 Next Steps

Ready to generate:
1. **LoginScreen.kt** - Email/password UI
2. **SignUpScreen.kt** - Signup with role selection
3. **NavRoutes.kt** - Navigation routes
4. **AppNavigation.kt** - NavHost with auth handling

Type **"continue"** or **"next"** to generate UI screens!

---

**📚 See Also:**
- `AUTH_VIEWMODEL_SUMMARY.md` - Detailed docs
- `DATA_LAYER_SUMMARY.md` - Data layer docs
- `GENERATION_STATUS.md` - Current progress


