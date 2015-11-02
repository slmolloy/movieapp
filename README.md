#movieapp
[![Build Status](https://travis-ci.org/slmolloy/movieapp.svg?branch=master)](https://travis-ci.org/slmolloy/movieapp)

Movieapp created for Udacity Developing Android Apps course project.

#Install
You will need to create and configure an API key for themoviedb.org.
http://themoviedb.org
Define a your API key as a system environment variable.

On Linux define a new file /etc/profile.d/themoviedbapikey.sh with the contents:
```
export THE_MOVIE_DB_API_KEY="<your api key>"
```
Logout and log back in for /etc/profile.d settings to take effect.

On other systems define an environment variable using best know methods.
The gradle build will look for the THE_MOVIE_DB_API_KEY environment variable.
