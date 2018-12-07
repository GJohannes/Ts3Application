# Ts3Application


# Server Side

## Server Log (Since the beginning)
- Loggs the time for all identities that joined the server. 
- Creates custom log files for user join/leave on server.

## MusicBot
1.0 Rudolf --->> 2.0 Lilith -->> 3.0 Kinzie -->> 4.0 Ajax

1. Ajax
    1. No Music bot changes
1. Kinzie:
    1. removed bug in which outdated vlc causes error stream to not start audio files
1. Lilith:
    1. bot now follows
    1. volume implementation
    1. additional webradios
    1. performance increase(file/command timeout 4h)
    1. ignore case sensitivity by commands
    1. ability to show history of commands
    1. ability to pull all users to requester
1. Rudolf:
   1. initial MusikBot version
   1. ability to play youtube links
   1. ability to play DasDing webradio 
   1. ability to show commands. 
   1. ability to stop current musik playing

## Riot Api (Since 2.1 Lilith)
Register a Summoner to check every 60 seconds if he won/lost a game. If so it will post the result as a Server message.
?help to get the commands
 -- (3.0 Kinzie) bugfix: Case incorrect name on entering the checkup periode no longer results in a always reported lost instead of win/lost

## Web Server (Since 4.0 Ajax)
A web server which runs on port 80 (HTTP-default) /  443 (HTTPS-Default). 

1. The webserver is capable of:
    1. Sending messages to the server (password protection)
    1. Sending private messages to each individual that is online on the server
    1. Showing how many people are on the server in the last 24 hours
    1. Showing a history of the number of people on the server (only if server - logger is activated)
    1. Displaying who is on the server at any given times
    1. Ajax calls to the server
    
# Client Side

# Music On Channel Switch (Since the beginning)
- Playes soundfile on a channel switch 
- !Requires additional Software!

# Lock user in Channel (Since the beginning)
- Under construction // GUI button only partially responding
