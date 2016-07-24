# Archillect

This is a simple app to turn an Android tablet into a picture frame that displays content from [Archillect](http://archillect.com/). Archillect is an AI that finds interesting images and redistributes them. It's really cool and you should totally check it out.

Since I like the idea of a picture frame with interesting images which I do not have to update myself (because I won't after the second or third time) I decided to use Archillect's feed. The most accessible way seemed to be to use its [twitter feed](https://twitter.com/archillect).

So I created a new project, dropped [Twitter4J](http://twitter4j.org/en/index.html) into it along with [Picasso](http://square.github.io/picasso/) to download and display the images and both [Bolts Tasks](https://github.com/BoltsFramework/Bolts-Android) and [Spork](http://sporklibrary.github.io/) to add a little streamlining and there it was (well, almost). Thank you for making my life easier!

## Getting it up and running

I didn't want to bother with creating a nice Twitter login et cetera so you'll just have to [create your own Twitter application](https://apps.twitter.com/) and create a personal access token (read only is enough) if you want to join in on the fun. In `/app/src/main/resources` you'll find `twitter4j.properties.sample`. Rename that to `twitter4j.properties` and fill in the tokens you got from Twitter. Hit run and you're set.

The app registers as a Launcher, so when your device next boots you get to choose if you want to start this app or any other launchers you might have installed. It's by far the easiest way to get an app to start on boot and since I made this for a tablet which I am going to be using solely for this purpose (for now, anyway), it suits my needs just fine. YMMV.

When the app's open, you can tap the screen to show a settings button which will open your device settings. This might come in handy, since you're now using this app as launcher. If you're using a device with an API level below 19 (4.4), you'll have to tap twice because that's how Android does things.

## Todo

* ~~Add readme~~
* A swanky icon
* ~~Start up the app on boot~~
* Crossfade images
* Add auto-on/off settings maybe

Don't be mad if I don't do my todo's. You're not my mum. Unless it is you in which case hi mum! You're the best <3.

## Sorry

Sorry that all the code is in the `MainActivity`. I don't think it's worth the bother to abstract those 100 lines away and end up with triple that amount of code. It does however irk me the tiniest of bits, so there you are. If you want to  though, have at it. The fork button is up there somewhere.

## License

MIT License

Copyright (c) 2016 Niels Keetlaer

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
