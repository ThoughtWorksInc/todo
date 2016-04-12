#!/bin/sh

git log --max-count=1 --format=format:%an | xargs -0 -n 1 git config --global -- user.name &&
git log --max-count=1 --format=format:%ae | xargs -0 -n 1 git config --global -- user.email &&

git config --global push.default simple &&

git branch --force gh-pages &&
git checkout gh-pages &&
git config remote.origin.url git@github.com:"$TRAVIS_REPO_SLUG".git &&

eval "$(ssh-agent -s)" &&
chmod 600 ./secret/id_rsa &&
ssh-add ./secret/id_rsa &&

git pull --no-edit origin gh-pages &&
sbt "set scalaJSStage in js := FullOptStage" indexHtml &&
git rm --ignore-unmatch deploy.sh .travis.yml &&
git add . &&
git commit -m 'Publish to GitHub Pages' &&
git push origin gh-pages:gh-pages
