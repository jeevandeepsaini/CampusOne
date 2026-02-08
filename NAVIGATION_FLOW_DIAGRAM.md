# 🗺️ CampusOne - Navigation Flow Diagram

## App Launch Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                         APP LAUNCH                               │
└───────────────────────────────┬─────────────────────────────────┘
                                │
                                ▼
                    ┌───────────────────────┐
                    │   AuthViewModel.init  │
                    │   Check AuthState     │
                    └───────────┬───────────┘
                                │
                ┌───────────────┼───────────────┐
                │               │               │
                ▼               ▼               ▼
        ┌──────────────┐ ┌────────────┐ ┌──────────────────┐
        │   Loading    │ │Unauth      │ │  Authenticated   │
        └──────┬───────┘ └─────┬──────┘ └─────────┬────────┘
               │               │                   │
               ▼               ▼                   ▼
          AuthGraph       AuthGraph        Check isAdmin
                                                   │
                                    ┌──────────────┼──────────────┐
                                    │                             │
                                    ▼                             ▼
                            ┌──────────────┐            ┌──────────────┐
                            │  isAdmin =   │            │  isAdmin =   │
                            │    false     │            │    true      │
                            └──────┬───────┘            └──────┬───────┘
                                   │                           │
                                   ▼                           ▼
                            ┌──────────────┐            ┌──────────────┐
                            │  USER GRAPH  │            │  ADMIN GRAPH │
                            └──────────────┘            └──────────────┘
```

---

## Auth Graph (Unauthenticated)

```
┌──────────────────────────────────────────────────┐
│              AUTH GRAPH                          │
├──────────────────────────────────────────────────┤
│                                                  │
│  ┌──────────────┐         ┌──────────────┐      │
│  │ LoginScreen  │◄───────►│ SignUpScreen │      │
│  │  (START)     │         │              │      │
│  └──────┬───────┘         └──────┬───────┘      │
│         │                        │              │
│         │  login()               │  signup()    │
│         └────────┬───────────────┘              │
│                  │                              │
│                  ▼                              │
│         AuthState.Authenticated                │
│                  │                              │
└──────────────────┼──────────────────────────────┘
                   │
                   ▼
            Switch to User/Admin Graph
```

---

## User Graph (Student/Professor)

```
┌────────────────────────────────────────────────────────────────┐
│                     USER GRAPH                                  │
├────────────────────────────────────────────────────────────────┤
│                                                                │
│  ┌───────────────┐                                            │
│  │  HomeScreen   │  ← START                                   │
│  │  (Dashboard)  │                                            │
│  └───────┬───────┘                                            │
│          │                                                    │
│    ┌─────┼─────┬──────────┬──────────┬──────────┐           │
│    │     │     │          │          │          │           │
│    ▼     ▼     ▼          ▼          ▼          ▼           │
│  ┌───┐ ┌───┐ ┌──────┐  ┌─────┐  ┌────────┐  ┌──────┐       │
│  │SOS│ │Cre-││My   │  │Announ││Report  │  │About │       │
│  │   │ │ate││Rpts │  │cements││Detail  │  │      │       │
│  └───┘ │Rpt││     │  │      ││{id}    │  └──────┘       │
│        └───┘└──┬──┘  └───┬──┘└────────┘                   │
│                │          │                                │
│                │          │                                │
│                ▼          ▼                                │
│          ┌──────────────────────┐                         │
│          │ ReportDetail/{id}    │                         │
│          └──────────────────────┘                         │
│                                                           │
│          ┌──────────────────────────┐                    │
│          │AnnouncementDetail/{id}   │                    │
│          └──────────────────────────┘                    │
│                                                           │
└───────────────────────────────────────────────────────────┘
```

---

## Admin Graph

```
┌────────────────────────────────────────────────────────────────┐
│                     ADMIN GRAPH                                 │
├────────────────────────────────────────────────────────────────┤
│                                                                │
│  ┌───────────────────┐                                        │
│  │ AdminHomeScreen   │  ← START                               │
│  │ (Admin Dashboard) │                                        │
│  └─────────┬─────────┘                                        │
│            │                                                  │
│    ┌───────┼────────┬──────────┬──────────┐                 │
│    │       │        │          │          │                 │
│    ▼       ▼        ▼          ▼          ▼                 │
│  ┌────┐ ┌─────┐  ┌──────┐  ┌──────┐  ┌──────┐              │
│  │All │ │Mana-│  │ SOS  │  │Detail│  │About │              │
│  │Rpts│ │ge   │  │      │  │      │  │      │              │
│  │    │ │Announ│└──────┘  └──────┘  └──────┘              │
│  │    │ │cements│                                          │
│  └─┬──┘ └───┬──┘                                           │
│    │        │                                              │
│    │        │  Create/Delete Announcements                │
│    │        ▼                                              │
│    │   ┌─────────────────────┐                           │
│    │   │ Create Announcement │                           │
│    │   └─────────────────────┘                           │
│    │                                                      │
│    │  Update Status (Pending/In Progress/Resolved)      │
│    ▼                                                      │
│  ┌─────────────────────────┐                            │
│  │ AllReportsScreen        │                            │
│  │ - View all reports      │                            │
│  │ - Filter by status      │                            │
│  │ - Update report status  │                            │
│  └─────────────────────────┘                            │
│                                                          │
└──────────────────────────────────────────────────────────┘
```

---

## Screen Access Matrix

| Screen                  | Student | Professor | Admin |
|------------------------|---------|-----------|-------|
| LoginScreen            | ✅      | ✅        | ✅    |
| SignUpScreen           | ✅      | ✅        | ✅    |
| HomeScreen             | ✅      | ✅        | ❌    |
| AdminHomeScreen        | ❌      | ❌        | ✅    |
| SosScreen              | ✅      | ✅        | ✅    |
| CreateReportScreen     | ✅      | ✅        | ❌    |
| MyReportsScreen        | ✅      | ✅        | ❌    |
| AllReportsScreen       | ❌      | ❌        | ✅    |
| AnnouncementsScreen    | ✅      | ✅        | ❌    |
| ManageAnnouncements    | ❌      | ❌        | ✅    |
| ReportDetail/{id}      | ✅      | ✅        | ✅    |
| AnnouncementDetail/{id}| ✅      | ✅        | ✅    |
| AboutScreen            | ✅      | ✅        | ✅    |

---

## Navigation Actions

### From Any Screen:
```kotlin
// Navigate to specific route
navController.navigate(Routes.Sos.route)

// Navigate with parameter
navController.navigate(Routes.ReportDetail.createRoute(reportId))

// Go back
navController.navigateUp()
navController.popBackStack()
```

### After Login/Signup:
```kotlin
// Navigation automatically switches based on AuthState change
authViewModel.login(email, password)
// → If successful: AuthState.Authenticated
// → NavHost observes state and switches graph automatically
// → If isAdmin: AdminGraph else: UserGraph
```

### Logout:
```kotlin
authViewModel.logout()
// → AuthState.Unauthenticated
// → NavHost switches to AuthGraph
// → Shows LoginScreen
```

---

## Route Parameters

### With Parameters:
```kotlin
// Define route with parameter placeholder
Routes.ReportDetail.route = "report_detail/{reportId}"

// Create route with actual value
val route = Routes.ReportDetail.createRoute("abc123")
// Result: "report_detail/abc123"

// In composable, extract parameter
val reportId = backStackEntry.arguments?.getString("reportId")
```

### Multiple Parameters:
```kotlin
// Could be extended like this:
object ReportEdit : Routes("report_edit/{reportId}/{mode}") {
    fun createRoute(reportId: String, mode: String) = 
        "report_edit/$reportId/$mode"
}
```

---

## Deep Linking (Future Enhancement)

```kotlin
// Could add deep links to routes:
composable(
    route = Routes.ReportDetail.route,
    arguments = listOf(navArgument("reportId") { type = NavType.StringType }),
    deepLinks = listOf(navDeepLink { 
        uriPattern = "campusone://report/{reportId}" 
    })
) { ... }
```

---

## Graph Switching Logic

```kotlin
// In AppNavigation.kt
val startDestination = when (authState) {
    AuthState.Loading -> Routes.AuthGraph.route
    AuthState.Unauthenticated -> Routes.AuthGraph.route
    AuthState.Authenticated -> {
        val authenticated = authState as AuthState.Authenticated
        if (authenticated.isAdmin) {
            Routes.AdminGraph.route  // Admin sees AdminHomeScreen
        } else {
            Routes.UserGraph.route   // User sees HomeScreen
        }
    }
}

NavHost(
    navController = navController,
    startDestination = startDestination
) { ... }
```

---

## Testing Navigation

### Test 1: Student Flow
```
1. Launch app → AuthState.Unauthenticated
2. LoginScreen shown
3. Login as student@campus.edu
4. AuthState.Authenticated(isAdmin=false)
5. Switch to UserGraph
6. HomeScreen shown ✓
```

### Test 2: Admin Flow
```
1. Launch app → AuthState.Unauthenticated
2. LoginScreen shown
3. Login as admin@gih.edu
4. AuthState.Authenticated(isAdmin=true)
5. Switch to AdminGraph
6. AdminHomeScreen shown ✓
```

### Test 3: Already Logged In
```
1. Launch app
2. AuthViewModel checks currentUser
3. Fetches role from Firestore
4. AuthState.Authenticated(isAdmin=?)
5. Start at appropriate graph
6. No login screen shown ✓
```

---

**Navigation setup is complete! Ready to implement screen UIs.** 🚀


