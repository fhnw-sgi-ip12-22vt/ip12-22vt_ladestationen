# IP12-ConnectnCharge

This repo is now archived, all further changes are to be done on [GitHub](https://github.com/fhnw-sgi-ip12-22vt/ip12-22vt_ladestationen).

The repo on GitHub is a fusion of this repository and the [docu repository](https://gitlab.fhnw.ch/ip12-22vt/ip12-22vt_ladestationen/docu), which is why it's a bit tricky to open it as a maven project in Intellij IDEA:

1. Open the repository folder (parent folder of the docu and connectncharge folders)
1. Quickly press shift twice
1. In the popup window, search `Add Maven Projects`

![search everywhere dialog](/uploads/2ebc3c0d8b57e19c5c0c7aa875094ac0/grafik.png)

4. Select the pom.xml file located at `connectncharge/pom.xml`
4. To test it, execute a maven goal like `package` in the maven sidebar on the right-hand-side. It should try to build the project.

![maven sidebar](/uploads/59244eeda6168476c34cf8de8c745a37/grafik.png)

For API-Documentation etcetera, visit
[Project Pages](https://ip12-22vt.pages.fhnw.ch/ip12-22vt_ladestationen/ip12-connectncharge)

## Workflow rules

Maintaining a clean repository is essential for the success of any project. One of the key elements to achieve this is
to handle every task in a separate branch that originates from the development branch. Once the task is completed, the
branch should be submitted as a pull request, and the team manager should conduct a code review. If no issues are found,
the pull request can be merged into the development branch. It's important to note that the main branch is reserved
exclusively for releases. By following these guidelines, you can ensure that your project stays organized, efficient,
and error-free.

## Getting started

```
git clone https://gitlab.fhnw.ch/ip12-22vt/ip12-22vt_ladestationen/ip12-connectncharge.git
git branch -M development
git push -uf origin development
```
