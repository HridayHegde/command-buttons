@echo off
REM CommandButtons Release Creator for Windows
REM Usage: scripts\create-release.bat [version]
REM Example: scripts\create-release.bat 1.0.1

setlocal enabledelayedexpansion

REM Check if version argument is provided
if "%~1"=="" (
    echo ❌ Error: Please provide a version number
    echo Usage: %0 [version]
    echo Example: %0 1.0.1
    exit /b 1
)

set VERSION=%~1

echo 🚀 Creating release for CommandButtons v%VERSION%

REM Check if we're in a git repository
if not exist ".git" (
    echo ❌ Error: This script must be run from the root of a git repository
    exit /b 1
)

REM Check if working directory is clean
git status --porcelain > temp_status.txt
for /f %%i in ("temp_status.txt") do set size=%%~zi
del temp_status.txt

if %size% gtr 0 (
    echo ⚠️  Warning: Working directory is not clean. Uncommitted changes detected:
    git status --short
    set /p "continue=Continue anyway? (y/N): "
    if /i not "!continue!"=="y" (
        echo ❌ Aborted
        exit /b 1
    )
)

REM Update version in gradle.properties
echo 📝 Updating version in gradle.properties...
powershell -Command "(gc gradle.properties) -replace 'mod_version=.*', 'mod_version=%VERSION%' | Out-File -encoding ASCII gradle.properties"

REM Build the project
echo 🔨 Building project...
if exist "gradlew.bat" (
    call gradlew.bat build
) else (
    gradle build
)

if errorlevel 1 (
    echo ❌ Error: Build failed
    exit /b 1
)

echo ✅ Build successful!

REM Commit version change
echo 📝 Committing version update...
git add gradle.properties
git commit -m "Bump version to %VERSION%"

REM Create and push tag
echo 🏷️  Creating and pushing tag v%VERSION%...
git tag -a "v%VERSION%" -m "Release version %VERSION%"
git push origin main
git push origin "v%VERSION%"

echo.
echo 🎉 Release v%VERSION% created successfully!
echo.
echo 📋 Next steps:
echo 1. GitHub Actions will automatically build and create the release
echo 2. Check the Actions tab on GitHub for build progress
echo 3. The release will appear at: https://github.com/HridayHegde/command-buttons/releases
echo 4. Announce the release to your community!
echo.
echo ⭐ Don't forget to ask users to star the repository!

endlocal
