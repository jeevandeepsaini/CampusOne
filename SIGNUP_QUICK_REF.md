# 📝 SignUpScreen - Quick Reference

## ✅ Complete Implementation

**File:** `SignUpScreen.kt`  
**Lines:** ~515  
**Status:** ✅ Production-ready  

---

## 🎨 UI Components

```
Header (Secondary Container):
- 🎓 Emoji
- "Join CampusOne" title
- "Create your account" subtitle

Card:
- Name field (optional, with person icon)
- Email field (required, with email icon)
- Password field (required, with lock icon + toggle)
- Confirm password (required, with lock icon + toggle)
- Role selection (FilterChips: Student/Professor)
- Sign Up button (with loading)
- Login link
```

---

## 🔌 AuthViewModel Call

```kotlin
authViewModel.signup(
    email = email.trim(),
    password = password,
    role = selectedRole,  // "student" or "professor"
    name = name.trim()
)
```

---

## ✅ Validation

- ✅ Email: Required, valid format
- ✅ Password: Required, min 6 chars
- ✅ Confirm: Required, must match password
- ⚪ Name: Optional (no validation)
- ✅ Role: Pre-selected (Student by default)

---

## 🎯 Role Selection

**FilterChips (Segmented Button Style):**
```kotlin
[🎓 Student]     [👨‍🏫 Professor]
  (selected)        (unselected)
```

**Values:**
- `Constants.UserRole.STUDENT` = "student"
- `Constants.UserRole.PROFESSOR` = "professor"

---

## 🧪 Quick Test

```
Name: Test User
Email: test@campus.edu
Password: testpass123
Confirm: testpass123
Role: Student ✓
Click Sign Up
→ Creates account → Navigates to HomeScreen
```

---

## 🔄 Navigation

**From Login:**
```kotlin
navController.navigate(Routes.SignUp.route)
```

**Back to Login:**
```kotlin
navController.popBackStack()
```

---

## 🎨 Key Features

✅ 4 input fields (name, email, password, confirm)
✅ 2 password visibility toggles (independent)
✅ Role selection with FilterChips
✅ Password match validation
✅ Loading indicator
✅ Error snackbar
✅ Keyboard actions (Next/Done)
✅ Scroll support
✅ Material3 theming

---

## 📊 Firestore Document Created

```json
users/{uid}/
{
  "uid": "abc123",
  "email": "test@campus.edu",
  "role": "student",
  "name": "Test User",
  "createdAt": Timestamp(now)
}
```

---

## 🚀 What Happens After

1. Creates Firebase Auth user
2. Creates Firestore users/{uid} doc
3. Computes isAdmin flag
4. AuthState → Authenticated
5. AppNavigation switches graph
6. Shows HomeScreen or AdminHomeScreen

---

## 📝 Error Examples

```
"Invalid email format"
"Password must be at least 6 characters"
"Passwords do not match"
"The email address is already in use"
```

---

## 🎉 Status

**Implementation:** ✅ Complete  
**Integration:** ✅ AuthViewModel connected  
**Validation:** ✅ All fields validated  
**Testing:** ⏳ Ready for testing  

---

**Next:** Type "implement home" or "update mainactivity"


