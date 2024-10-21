# Github convention

## Overview

- This is a convention for using Github in a team project
- This convention is based on the [GitFlow](https://nvie.com/posts/a-successful-git-branching-model/) model
- This convention is for a team project, not for a personal project
- This convention is for a project that uses the `master` branch as the production branch and the `develop` branch as the development branch
- This convention is for a project that uses the `dev` branch for adding new features and the `fix` branch for fixing bugs

## Branches

- `master`: the production branch
- `develop`: the development branch
- `dev/feature-name`: the branch for adding new features
- `fix/bug-name`: the branch for fixing bugs

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

Remove the branch on the local repository

- `git branch -d dev/feature-name`
- `git branch -d fix/bug-name`

And then delete the branch on the remote repository

- `git push origin --delete dev/feature-name`
- `git push origin --delete fix/bug-name`

8. Update the local repository

- `git pull`

9. Repeat the process
10. Finish the project