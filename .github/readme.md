# MovieShowcase
Demo application that shows a library of recent movies that can be favourite and rated

## Building
Before building the application you need an api key, you can retrieve the api key from your 
[TMDB Account settings page](https://www.themoviedb.org/settings/api)

The key must be saved somewhere as a string name `api_key`, it is recommended to store this key in 
another file (e.g. keys.xml) and add that file into your .gitignore

Example of `keys.xml` file

```xml
<resources>
    <string name="api_key">123456789abcdef</string>
</resources>
```