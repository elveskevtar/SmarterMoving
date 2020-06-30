Contributing
------------
In order to maintain an organized project for an already complex mod, there will be a
signficiant emphasis placed on standard operating procedures including contributions.

#### Rules to live by:
1. Work off of an existing issue or create one; otherwise you may be working on
something that is either low priority or not a priority at all. Issues will guide the
conversation for large stories/epics to small tasks/sub-tasks.
2. Submit Pull Requests with clean commits and descriptive messages. Most PRs should
only need one commit so please
[squash](https://www.internalpointers.com/post/squash-commits-into-one-git) unnecessary
commits.
3. In some cases such as large code changes spanning multiple core components of
the mod, it may make sense to have multiple commits. Separate these commits based on
the respective components and include descriptive messages.
4. The rule of thumb is: if it cannot be put into its own separate PR and still make
sense, it should be squashed
5. Be respectful to other contributors and maintainers. We are all here to create an
amazing mod and need to maintain a collaborative space.
6. Join the discord to be up to date on the current alpha/beta releases and what is
currently happening with the project. This is where the majority of detailed discussion
will take place. If you are very serious about contributing, this is the place to be.
7. Squash, squash, squash

Note: for those new to squashing/rebasing, be careful! Rebasing actually modifies the
Git commit history, something not applicable to a standard merge workflow. If you do
not know what you are doing at first, spend some time reading up on Git rebasing so
that you don't end up putting your local repository into a weird state or, worse yet,
deleting yours/others recent commits. The perks of rebasing is that it keeps the Git
history very clean and maintainable, it removes the need for those pesky merge commits,
and it has practically become an industry standard - useful technical skill.

#### How to start contributing:
1. Create a fork
2. Identify issue(s) to work on
3. Clone your fork's repository locally
4. Make changes locally on whatever branch you want
5. Once done, pull from my master branch and rebase on top
```
git remote add mainline https://github.com/Elveskevtar/SmarterMoving.git # only need to do this if you have not added the remote already
git fetch mainline
git rebase mainline/master # fix merge conflicts if necessary and git rebase --continue
git push # push to your fork and then submit a PR through GitHub
```
6. Submit a PR for your changes - make sure it's the right branch

Note: if you have some work pushed to your fork and then you rebase on top of some old
commits from mainline/master, you may find that you will be some commits ahead but also
some commits behind. This means you would have to force push from your local branch with
`git push -f`. Be careful if you don't know what you're doing and double check the git
log if you are not sure. Moral of the story is to learn Git!
