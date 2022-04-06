![logo](https://imgur.com/q2Y6QEw.jpg)

<div align="center">

[server-invite]: https://discord.gg/65mmyX8QEn
[usercount]: https://canary.discord.com/api/guilds/867101984888061952/widget.png
[viewcounter]: https://hitcounter.pythonanywhere.com/count/tag.svg?url=https://github.com/4l3xh4n53n/SpringBOT
[jetbrains-badge]: https://img.shields.io/badge/Powered%20by%20JetBrains-gray.svg?logo=webstorm
[jetbrains-url]: https://www.jetbrains.com/?from=switchblade/
[idea-url]: https://www.jetbrains.com/idea/download/#section=windows
[java-url]: https://www.java.com/en/
[sqlite-url]: https://www.sqlite.org/index.html

[ ![usercount][] ][server-invite]
![Views][viewcounter]
[![Maintenance](https://img.shields.io/badge/Maintained%3F-yes-green.svg)](https://GitHub.com/4l3xh4n53n/SpringBOT/graphs/commit-activity)
![Maintainer](https://img.shields.io/badge/maintainer-4l3xh4n53n-blue)
[![Ask Me Anything !](https://img.shields.io/badge/Ask%20me-anything-1abc9c.svg)][server-invite]
[![Open Source? Yes!](https://badgen.net/badge/Open%20Source%20%3F/Yes%21/blue?icon=github)](https://github.com/Naereen/badges/)
[![jetbrains][jetbrains-badge]][jetbrains-url]
[![idea](https://img.shields.io/badge/IntelliJIDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)][idea-url]
[![java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)][java-url]
[![sqlite](https://img.shields.io/badge/SQLite-07405E?style=for-the-badge&logo=sqlite&logoColor=white)][sqlite-url]
</div>

# SPRING IS MOVING -> 

[Spring Rebrand + re program](https://github.com/4l3xh4n53n/Oregano)

# SpringBOT
This is Spring BOT, Spring say hi :) Spring is a multipurpose discord bot which supports some usefull features, like invite logger, welcome messages, polls, basic modderation commands, and you can even be rewarded COINS for being active in your favourite server.
Spring bot is also modular, supporting the ability to turn features on and off and allow certain roles to do certain things. You can even change the colour of the embeds!

## Branches
Master - The one the bot is currently running
Development - What I am working on

# Creating a runnable jar

First you have to create a file called `TOKEN` in the source folder and put your discord bots token in it.

THEN

Go into `src/main/java/Core/Main.java` and follow the instructions in there to make the code not try to scale the bot,
also go into `src/main/java/Core/Databases.java` and comment the mariaDB methods and uncomment the sqlite methods ( unless of course you want to migrate the sqlite to mariaDB)

THEN

Run the sh shell script
```
./compile.sh
```
Then run the .jar !!!! It's that simple
```
java -jar Spring-1.2.0-jar-with-dependencies.jar
```
Make sure you are running it under java 11 and that the Database folder stays in the same directory as the jar file.
