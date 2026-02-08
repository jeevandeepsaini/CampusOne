# 📋 CampusOne - Manual Testing Checklist

## 🚀 Pre-Testing Setup

### ✅ Firebase Configuration
- [ ] `google-services.json` placed in `app/` folder
- [ ] Firebase project created in Firebase Console
- [ ] Firebase Authentication enabled (Email/Password)
- [ ] Firestore Database created
- [ ] Firestore rules configured (see below)
- [ ] App SHA-1 fingerprint added to Firebase (for release builds)

### ✅ Image Assets
- [ ] Developer photo placed at `app/src/main/res/drawable/my_photo.jpg`

### ✅ Build & Sync
- [ ] Gradle sync successful
- [ ] No compilation errors
- [ ] App builds successfully

---

## 📱 Testing Flow

## 1️⃣ AUTHENTICATION TESTING

### **Test 1.1: Sign Up (Student)**
**Steps:**
1. Launch app
2. Should show LoginScreen
3. Tap "Sign Up" link
4. Fill in:
   - Name: "Test Student" (optional)
   - Email: `student@test.com`
   - Password: `test123`
   - Confirm Password: `test123`
   - Role: Select "Student"
5. Tap "Sign Up"

**Expected Results:**
- ✅ Shows "Creating Account..." loading indicator
- ✅ Navigates to HomeScreen
- ✅ Welcome message shows "Welcome back, student"
- ✅ Role shows "Student"
- ✅ Firestore creates document: `users/{uid}` with role="student"

**Common Issues:**
- ❌ "Email already in use" → Use different email
- ❌ Network error → Check internet connection
- ❌ Firebase error → Check Firebase console settings

---

### **Test 1.2: Sign Up (Professor)**
**Steps:**
1. Logout from student account
2. Tap "Sign Up"
3. Fill in:
   - Email: `professor@test.com`
   - Password: `test123`
   - Confirm Password: `test123`
   - Role: Select "Professor"
4. Tap "Sign Up"

**Expected Results:**
- ✅ Navigates to HomeScreen
- ✅ Welcome message shows "Welcome back, professor"
- ✅ Role shows "Professor"

---

### **Test 1.3: Sign Up (Admin)**
**Steps:**
1. Logout
2. Tap "Sign Up"
3. Fill in:
   - Email: `admin@gih.edu` (must be in ADMIN_EMAILS list)
   - Password: `admin123`
   - Confirm Password: `admin123`
   - Role: Select "Professor"
4. Tap "Sign Up"

**Expected Results:**
- ✅ Navigates to **AdminHomeScreen** (not regular HomeScreen)
- ✅ Admin features visible

**Admin Emails (hardcoded):**
- `admin@gih.edu`
- `admin@campus.edu`

---

### **Test 1.4: Login (Existing User)**
**Steps:**
1. Logout
2. Enter:
   - Email: `student@test.com`
   - Password: `test123`
3. Tap "Login"

**Expected Results:**
- ✅ Shows "Logging in..." indicator
- ✅ Navigates to HomeScreen
- ✅ User info displayed correctly

---

### **Test 1.5: Login Validation**
**Test Invalid Email:**
1. Email: `notanemail`
2. Tap "Login"
3. ✅ Should show: "Invalid email format"

**Test Short Password:**
1. Email: `test@test.com`
2. Password: `123`
3. Tap "Login"
4. ✅ Should show: "Password must be at least 6 characters"

**Test Wrong Credentials:**
1. Email: `student@test.com`
2. Password: `wrongpass`
3. Tap "Login"
4. ✅ Should show Firebase error in snackbar

---

### **Test 1.6: Logout**
**Steps:**
1. From HomeScreen, tap logout icon (top right)
2. Confirm logout in dialog
3. Tap "Logout"

**Expected Results:**
- ✅ Returns to LoginScreen
- ✅ User signed out from Firebase
- ✅ Cannot navigate back to home

---

## 2️⃣ EMERGENCY SOS TESTING

### **Test 2.1: View Emergency Contacts**
**Steps:**
1. Login as student
2. From HomeScreen, tap "Emergency SOS" card
3. Should show SosScreen

**Expected Results:**
- ✅ Shows 3 emergency contacts:
  - 🛡️ Campus Security
  - 🏥 Medical Emergency
  - 👨‍💼 Campus Warden
- ✅ Each has phone number and "Call" button
- ✅ Shows "Open Campus in Maps" button
- ✅ Shows national emergency numbers (100, 101, 102, 112)

---

### **Test 2.2: Call Emergency Contact**
**Steps:**
1. On SosScreen
2. Tap "Call" button on Campus Security

**Expected Results:**
- ✅ Opens phone dialer app
- ✅ Number pre-filled: +91-XXX-XXX-1234
- ✅ Does NOT automatically call (user must confirm)

---

### **Test 2.3: Open Maps**
**Steps:**
1. Tap "Open Campus in Maps"

**Expected Results:**
- ✅ Opens Google Maps (if installed)
- ✅ Shows campus location pin
- **OR** Opens browser with Google Maps web (if Maps not installed)

---

## 3️⃣ REPORTS TESTING

### **Test 3.1: Create Report**
**Steps:**
1. From HomeScreen, tap "Report an Issue" card
2. Should show CreateReportScreen
3. Fill in:
   - Category: Select "Infrastructure"
   - Description: "Broken window in main building needs repair urgently"
   - Location: "Main Building, Room 101" (optional)
4. Tap "Submit Report"

**Expected Results:**
- ✅ Shows "Submitting..." loading indicator
- ✅ Navigates to MyReportsScreen
- ✅ New report appears in list with "Pending" status (red)
- ✅ Firestore creates document in `reports` collection

**Firestore Document:**
```json
{
  "category": "Infrastructure",
  "description": "Broken window in...",
  "location": "Main Building, Room 101",
  "status": "Pending",
  "createdAt": Timestamp(now),
  "updatedAt": Timestamp(now),
  "createdByUid": "abc123",
  "createdByEmail": "student@test.com",
  "createdByRole": "student"
}
```

---

### **Test 3.2: Create Report Validation**
**Test Missing Category:**
1. Leave category blank
2. Fill description
3. Tap "Submit Report"
4. ✅ Shows: "Please select a category"

**Test Short Description:**
1. Select category
2. Type: "Test" (less than 10 chars)
3. Tap "Submit Report"
4. ✅ Shows: "Description must be at least 10 characters"

---

### **Test 3.3: View My Reports**
**Steps:**
1. From HomeScreen, tap "My Reports" button
2. Should show MyReportsScreen

**Expected Results:**
- ✅ Shows list of user's reports
- ✅ Each card shows:
  - Category icon and name
  - Status chip (colored)
  - Description preview (2 lines)
  - Location (if provided)
  - Submission date
- ✅ Can tap report to view details
- ✅ FAB button to create new report
- ✅ Real-time updates (new reports appear automatically)

---

### **Test 3.4: View Report Details**
**Steps:**
1. From MyReportsScreen
2. Tap any report card

**Expected Results:**
- ✅ Shows ReportDetailScreen
- ✅ Displays:
  - Status card with icon (⏳ Pending)
  - Category with emoji
  - Full description
  - Location (if provided)
  - Timeline (submitted + last updated dates)
  - Submitted by info (email + role)

---

### **Test 3.5: Admin View All Reports**
**Steps:**
1. Logout, login as admin (`admin@gih.edu`)
2. Navigate to AllReportsScreen (from admin menu)

**Expected Results:**
- ✅ Shows ALL reports from ALL users
- ✅ Stats card shows counts:
  - ⏳ Pending: X
  - 🔄 In Progress: Y
  - ✅ Resolved: Z
- ✅ Each report shows submitter info (username + role badge)
- ✅ Each report has status dropdown

---

### **Test 3.6: Admin Update Report Status**
**Steps:**
1. On AllReportsScreen (as admin)
2. Find a "Pending" report
3. Tap status button: [⏳ Pending ▼]
4. Select "In Progress"

**Expected Results:**
- ✅ Dropdown closes
- ✅ Snackbar shows: "Status updated to In Progress"
- ✅ Status button changes to: [🔄 In Progress ▼] (blue color)
- ✅ Firestore updates document
- ✅ User's MyReportsScreen also updates automatically!

**Test All Status Changes:**
- Pending → In Progress ✅
- In Progress → Resolved ✅
- Resolved → Pending ✅

---

## 4️⃣ ANNOUNCEMENTS TESTING

### **Test 4.1: View Announcements (User)**
**Steps:**
1. Login as student
2. From HomeScreen, tap "Announcements" card
3. Should show AnnouncementsScreen

**Expected Results:**
- ✅ Shows list of all campus announcements
- ✅ Each card shows:
  - 📢 Campaign icon
  - Title
  - Message preview (3 lines)
  - Posted date
  - "Read More" link
- ✅ Empty state if no announcements

---

### **Test 4.2: View Announcement Details**
**Steps:**
1. From AnnouncementsScreen
2. Tap any announcement card

**Expected Results:**
- ✅ Shows AnnouncementDetailScreen
- ✅ Displays:
  - Large title card with icon
  - Posted date card (with time)
  - Full message card
  - "Official campus announcement" banner

---

### **Test 4.3: Admin Create Announcement**
**Steps:**
1. Login as admin
2. Navigate to ManageAnnouncementsScreen
3. Tap FAB (+) button
4. Dialog opens
5. Fill in:
   - Title: "Campus Event"
   - Message: "Annual sports day will be held on February 15, 2026. All students are invited."
6. Tap "Create"

**Expected Results:**
- ✅ Validates: Title ≥3 chars, Message ≥10 chars
- ✅ Shows "Creating..." loading indicator
- ✅ Dialog closes
- ✅ Snackbar shows: "Announcement created successfully"
- ✅ New announcement appears in list
- ✅ Firestore creates document in `announcements` collection
- ✅ All users' screens update automatically!

---

### **Test 4.4: Admin Create Announcement Validation**
**Test Short Title:**
1. Title: "Hi" (2 chars)
2. Tap "Create"
3. ✅ Shows: "Title must be at least 3 characters"

**Test Short Message:**
1. Title: "Test Title"
2. Message: "Test" (4 chars)
3. Tap "Create"
4. ✅ Shows: "Message must be at least 10 characters"

---

### **Test 4.5: Admin Delete Announcement**
**Steps:**
1. On ManageAnnouncementsScreen (as admin)
2. Find any announcement
3. Tap delete icon (🗑️)
4. Confirmation dialog appears
5. Tap "Delete"

**Expected Results:**
- ✅ Dialog closes
- ✅ Snackbar shows: "Announcement deleted"
- ✅ Announcement disappears from list
- ✅ Firestore deletes document
- ✅ All users' screens update (announcement removed)

---

## 5️⃣ ABOUT SCREEN TESTING

### **Test 5.1: View About Screen**
**Steps:**
1. From HomeScreen, tap "About Us" button
2. Should show AboutScreen

**Expected Results:**
- ✅ Shows:
  - App logo (🎓) and info
  - App description
  - Developer card with:
    - Circular photo (if placed)
    - Name: "Jeevandeep Saini"
    - GIH ID badge: "GIH020JEE"
    - Institute: "The NorthCap University"
  - Hackathon submission card
  - Features list
  - Copyright notice
- ✅ Scrollable content
- ✅ Back button works

---

## 6️⃣ REAL-TIME SYNC TESTING

### **Test 6.1: Two-Device Report Status Sync**
**Setup:** 2 devices (or emulators)
1. Device A: Login as student
2. Device B: Login as admin

**Steps:**
1. Device A: Create a report (Status: Pending)
2. Device B: Open AllReportsScreen
3. ✅ New report appears automatically
4. Device B: Change status to "In Progress"
5. Device A: Open MyReportsScreen
6. ✅ Status updates automatically (no refresh needed!)

---

### **Test 6.2: Two-Device Announcement Sync**
**Setup:** 2 devices
1. Device A: Login as student, open AnnouncementsScreen
2. Device B: Login as admin, open ManageAnnouncementsScreen

**Steps:**
1. Device B: Create announcement "Test Event"
2. Device A: Watch AnnouncementsScreen
3. ✅ New announcement appears automatically!
4. Device B: Delete announcement
5. Device A: Watch screen
6. ✅ Announcement disappears automatically!

---

## 7️⃣ NAVIGATION TESTING

### **Test 7.1: User Navigation Flow**
**Steps:**
1. Login as student
2. HomeScreen → Emergency SOS → Back
3. HomeScreen → Report an Issue → Submit → MyReportsScreen → Back
4. HomeScreen → Announcements → Tap announcement → Back
5. HomeScreen → My Reports → Tap report → Back
6. HomeScreen → About Us → Back
7. HomeScreen → Logout

**Expected Results:**
- ✅ All navigation works smoothly
- ✅ Back button returns to previous screen
- ✅ No crashes

---

### **Test 7.2: Admin Navigation Flow**
**Steps:**
1. Login as admin
2. AdminHomeScreen → All Reports → Tap report → Back
3. AdminHomeScreen → Manage Announcements → Create → Back
4. Logout

**Expected Results:**
- ✅ Admin-specific screens accessible
- ✅ Navigation works correctly

---

## 8️⃣ ERROR HANDLING TESTING

### **Test 8.1: Network Error**
**Steps:**
1. Turn off WiFi/Data
2. Try to create report
3. ✅ Shows error snackbar

**Recovery:**
1. Turn on WiFi/Data
2. Try again
3. ✅ Should work

---

### **Test 8.2: Empty States**
**Test Empty Reports:**
1. New user (no reports)
2. Open MyReportsScreen
3. ✅ Shows empty state with "Create First Report" button

**Test Empty Announcements:**
1. Admin deletes all announcements
2. Open AnnouncementsScreen
3. ✅ Shows empty state

---

## ✅ Final Checklist

### **Authentication:**
- [ ] Sign up (Student) works
- [ ] Sign up (Professor) works
- [ ] Sign up (Admin) works
- [ ] Login works
- [ ] Logout works
- [ ] Validation works

### **Emergency SOS:**
- [ ] View contacts works
- [ ] Call button opens dialer
- [ ] Maps button works

### **Reports:**
- [ ] Create report works
- [ ] View my reports works
- [ ] View report details works
- [ ] Admin view all reports works
- [ ] Admin update status works
- [ ] Real-time sync works

### **Announcements:**
- [ ] View announcements works
- [ ] View announcement details works
- [ ] Admin create announcement works
- [ ] Admin delete announcement works
- [ ] Real-time sync works

### **Other:**
- [ ] About screen displays correctly
- [ ] Navigation works
- [ ] Back button works
- [ ] No crashes

---

## 📸 Test User Accounts

**Create these test accounts:**

```
Student:
Email: student@test.com
Password: test123
Role: Student

Professor:
Email: professor@test.com
Password: test123
Role: Professor

Admin:
Email: admin@gih.edu
Password: admin123
Role: Professor (but gets admin access)
```

---

## 🎯 Success Criteria

**App is working correctly if:**
✅ All authentication flows work
✅ All CRUD operations work (Create, Read, Update, Delete)
✅ Real-time updates work without manual refresh
✅ Navigation works smoothly
✅ No crashes during normal usage
✅ Error messages are clear and helpful
✅ UI is responsive and looks good

---

## 📝 Bug Reporting Template

**If you find a bug:**
```
Bug: [Short description]
Steps to Reproduce:
1. Step 1
2. Step 2
3. Step 3

Expected: [What should happen]
Actual: [What actually happened]
Error Message: [If any]
Screen: [Which screen]
User Role: [Student/Professor/Admin]
```

---

**🎉 Happy Testing! This checklist ensures your CampusOne app works perfectly!**

