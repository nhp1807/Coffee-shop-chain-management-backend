# Github convention

## Overview

## Convention

### Branch naming convention

- `dev/feature-name`: for new features
- `fix/bug-name`: for bug fixes

### Commit message convention

- `Add: feature-name`: for adding new features
- `Fix: bug-name`: for fixing bugs
- `Update: feature-name`: for updating features

### Pull request convention

- `Add: feature-name`: for adding new features
- `Fix: bug-name`: for fixing bugs
- `Update: feature-name`: for updating features

### Step-by-step guide

1. Create a new branch

- `git checkout -b dev/feature-name`
- `git checkout -b fix/bug-name`

2. Add new features or fix bugs

- `git add .`

3. Commit changes

- `git commit -m "Add: feature-name"`

4. Push to the remote repository

- `git push origin dev/feature-name`
- `git push origin fix/bug-name`

5. Create a pull request

- Make a pull request from the new branch to the develop branch
- Mustn't make a pull request from the new branch to the master branch
- Mustn't merge the pull request without approval

6. Review and merge
7. Delete the branch

- `git branch -d dev/feature-name`
- `git branch -d fix/bug-name`

8. Update the local repository

- `git pull`

9. Repeat the process
10. Finish the project
