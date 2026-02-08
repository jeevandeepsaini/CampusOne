# 🚀 GitHub Upload Guide - CampusOne

## ✅ Pre-Upload Checklist

### **BEFORE You Push to GitHub:**

- [x] `.gitignore` created and configured ✅
- [ ] Remove `google-services.json` (already in .gitignore)
- [ ] Remove any keystore files
- [ ] Remove local.properties
- [ ] Check no API keys in code
- [ ] Test build works
- [ ] README ready

---

## 🔒 Security Check

### **FILES THAT WILL NOT BE UPLOADED** (Protected by .gitignore):

```
✅ google-services.json       # Firebase config
✅ local.properties           # SDK paths
✅ *.keystore / *.jks        # Signing keys
✅ build/ folders            # Build outputs
✅ .idea/ (most files)       # IDE settings
✅ secrets.properties        # API keys
```

### **FILES THAT WILL BE UPLOADED:**

```
✅ Source code (.kt files)
✅ Gradle files
✅ XML resources
✅ AndroidManifest.xml
✅ Documentation (.md files)
✅ .gitignore
```

---

## 📝 Steps to Upload to GitHub

### **Step 1: Initialize Git Repository**

```bash
cd "C:\Users\Jeevandeep Saini\AndroidStudioProjects\GIH\CampusOne"
git init
```

### **Step 2: Add Files**

```bash
git add .
```

This will add all files EXCEPT those in `.gitignore`

### **Step 3: First Commit**

```bash
git commit -m "Initial commit: CampusOne - Smart Campus Solution

- Complete Android app with Jetpack Compose
- Firebase Authentication & Firestore
- Role-based access (Student/Professor/Admin)
- Features: SOS, Reports, Announcements
- Material Design 3 UI
- Submission for Great Indian Hackathon 2026"
```

### **Step 4: Create GitHub Repository**

1. Go to: https://github.com/new
2. Repository name: `CampusOne` or `campus-one`
3. Description: "Smart Campus Solution - Android app for Great Indian Hackathon 2026"
4. Choose: **Public** (for hackathon submission)
5. **DON'T** initialize with README (we already have one)
6. Click "Create repository"

### **Step 5: Connect to GitHub**

Replace `yourusername` with your GitHub username:

```bash
git remote add origin https://github.com/yourusername/CampusOne.git
```

### **Step 6: Rename Branch (Optional but Recommended)**

```bash
git branch -M main
```

### **Step 7: Push to GitHub**

```bash
git push -u origin main
```

You'll be prompted for GitHub credentials. Use:
- Username: Your GitHub username
- Password: Your Personal Access Token (not your password!)

---

## 🔑 GitHub Personal Access Token

If you don't have a token:

1. Go to: https://github.com/settings/tokens
2. Click: "Generate new token" → "Generate new token (classic)"
3. Select scopes:
   - ✅ `repo` (Full control of private repositories)
4. Click "Generate token"
5. **COPY THE TOKEN** (you won't see it again!)
6. Use this token as your password when pushing

---

## 📄 README Setup

You have two README files:

1. **README.md** - Setup guide (current)
2. **README_GITHUB.md** - Professional GitHub README

**Recommendation:** Rename for GitHub:

```bash
# Backup current setup guide
mv README.md SETUP_GUIDE.md

# Use GitHub README as main
mv README_GITHUB.md README.md

# Add and commit
git add .
git commit -m "docs: Update README for GitHub"
git push
```

---

## 🎨 Optional: Add Topics to GitHub Repo

After uploading, add these topics on GitHub:

```
android
kotlin
jetpack-compose
firebase
material-design-3
campus-management
hackathon
mvvm-architecture
kotlin-coroutines
firestore
```

---

## 📸 Optional: Add Screenshots

Create a `screenshots/` folder:

```bash
mkdir screenshots
```

Add screenshots of:
- Login screen
- Home screen
- Report creation
- My reports
- Admin dashboard
- Announcements

Then update README with:
```markdown
## 📸 Screenshots

<img src="screenshots/login.png" width="250"> <img src="screenshots/home.png" width="250">
```

---

## 🔗 Update README Links

After creating GitHub repo, update these in README:

1. Replace `yourusername` with actual username
2. Add your email
3. Add LinkedIn (optional)
4. Update issue links
5. Add repository URL

---

## ✅ Verification Checklist

After pushing to GitHub:

- [ ] Repository is public
- [ ] All source files visible
- [ ] `google-services.json` NOT visible
- [ ] No keystore files visible
- [ ] README displays correctly
- [ ] Topics added
- [ ] Description set
- [ ] License added (optional)

---

## 🎯 Repository Settings (On GitHub)

### **Recommended Settings:**

1. **About Section:**
   - Description: "Smart Campus Solution - Android app for campus operations"
   - Website: (if you have one)
   - Topics: android, kotlin, jetpack-compose, firebase

2. **Social Preview Image:**
   - Upload app icon or screenshot (1280×640px)

3. **Features:**
   - ✅ Issues
   - ✅ Discussions (optional)
   - ✅ Projects (optional)

---

## 📧 After Upload

### **Share Repository:**

For hackathon submission, share:
```
GitHub: https://github.com/yourusername/CampusOne
```

### **Important Notes for Judges:**

Create a `HACKATHON_NOTES.md`:

```markdown
# 🏆 Hackathon Submission Notes

## Firebase Setup Required

This app requires Firebase configuration. To run:

1. Create Firebase project
2. Add google-services.json (not included for security)
3. Enable Authentication & Firestore
4. See SETUP_GUIDE.md for details

## Demo Credentials

Student: student@test.com / test123
Admin: admin@gih.edu / admin123

## Video Demo

[Link to video demo if available]

## Contact

Developer: Jeevandeep Saini
GIH ID: GIH020JEE
Email: [Your Email]
```

---

## 🐛 Troubleshooting

### **Issue: "Permission denied"**
```bash
# Check remote URL
git remote -v

# Fix if wrong
git remote set-url origin https://github.com/yourusername/CampusOne.git
```

### **Issue: "Large files"**
```bash
# Check file sizes
git ls-files -s | awk '{print $4}' | xargs du -h

# If needed, add to .gitignore and commit
```

### **Issue: "Already up to date"**
```bash
# Force push (careful!)
git push -f origin main
```

---

## 🎉 Success!

Your repository is now live on GitHub! 🚀

Next steps:
1. ✅ Verify everything uploaded correctly
2. ✅ Add screenshots (optional)
3. ✅ Share link with hackathon organizers
4. ✅ Add to your portfolio
5. ✅ Share on LinkedIn

---

**Good luck with the Great Indian Hackathon 2026! 🏆**

