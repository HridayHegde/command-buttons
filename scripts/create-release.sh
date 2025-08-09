#!/bin/bash

# CommandButtons Release Creator
# Usage: ./scripts/create-release.sh [version]
# Example: ./scripts/create-release.sh 1.0.1

set -e

# Check if version argument is provided
if [ $# -eq 0 ]; then
    echo "❌ Error: Please provide a version number"
    echo "Usage: $0 [version]"
    echo "Example: $0 1.0.1"
    exit 1
fi

VERSION=$1

# Validate version format (basic check)
if [[ ! $VERSION =~ ^[0-9]+\.[0-9]+\.[0-9]+$ ]]; then
    echo "❌ Error: Invalid version format. Use semantic versioning (e.g., 1.0.1)"
    exit 1
fi

echo "🚀 Creating release for CommandButtons v$VERSION"

# Check if we're in a git repository
if [ ! -d ".git" ]; then
    echo "❌ Error: This script must be run from the root of a git repository"
    exit 1
fi

# Check if working directory is clean
if [[ -n $(git status --porcelain) ]]; then
    echo "⚠️  Warning: Working directory is not clean. Uncommitted changes detected:"
    git status --short
    read -p "Continue anyway? (y/N): " -n 1 -r
    echo
    if [[ ! $REPLY =~ ^[Yy]$ ]]; then
        echo "❌ Aborted"
        exit 1
    fi
fi

# Update version in gradle.properties
echo "📝 Updating version in gradle.properties..."
sed -i.bak "s/mod_version=.*/mod_version=$VERSION/" gradle.properties

# Check if version was updated successfully
if ! grep -q "mod_version=$VERSION" gradle.properties; then
    echo "❌ Error: Failed to update version in gradle.properties"
    mv gradle.properties.bak gradle.properties 2>/dev/null || true
    exit 1
fi

# Remove backup file
rm -f gradle.properties.bak

# Build the project
echo "🔨 Building project..."
if command -v ./gradlew &> /dev/null; then
    ./gradlew build
elif command -v gradle &> /dev/null; then
    gradle build
else
    echo "❌ Error: Gradle not found. Please install Gradle or use the wrapper."
    exit 1
fi

# Check if build was successful
if [ $? -ne 0 ]; then
    echo "❌ Error: Build failed"
    exit 1
fi

echo "✅ Build successful!"

# Commit version change
echo "📝 Committing version update..."
git add gradle.properties
git commit -m "Bump version to $VERSION"

# Create and push tag
echo "🏷️  Creating and pushing tag v$VERSION..."
git tag -a "v$VERSION" -m "Release version $VERSION"
git push origin main
git push origin "v$VERSION"

echo ""
echo "🎉 Release v$VERSION created successfully!"
echo ""
echo "📋 Next steps:"
echo "1. GitHub Actions will automatically build and create the release"
echo "2. Check the Actions tab on GitHub for build progress"
echo "3. The release will appear at: https://github.com/HridayHegde/command-buttons/releases"
echo "4. Announce the release to your community!"
echo ""
echo "⭐ Don't forget to ask users to star the repository!"
