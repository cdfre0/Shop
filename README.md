## Overview
This project is an implementation of Online shop selling digital games, in which user can look up avaliable games, search for games of favourite developer or even your favourite genre.
Program uses accounts of user and admin. Admin can manage database of users and promote users 
User can search for games, check if shop has it in stock, and buy it.
By special calls admin can update price of game, quantity in stock, add new games to repository either with or without existing stocks, and delete game by id, or all of them that do not have avaliable copy.

## DATA
Types of Genre avaliable:
1. SHOOTER
2. RPG
3. PLATFORM
4. STRATEGY
5. OPENWORLD
6. SIMULATION
7. RTS
8. PUZZLE
9. ACTION
10. HORROR

Game Structure in JSON
{
    "id": 1,
    "name": "Resident Evil 4",
    "developer": "CAPCOM",
    "price": 200.0,
    "quantity": 5,
    "genre": [
        "HORROR",
        "ACTION"
    ]
}
User Structure in JSON
{   "login": "login",
    "password": "password",
    "permission": true  }


Game structure that can be passed in POST call:
1. 
{  "name": "Resident Evil 4",
    "developer": "CAPCOM",
    "genre": 
    [   "HORROR"  ],
    "price": 200.0,  }

2.  
{   "name": "Resident Evil 4",
    "developer": "CAPCOM",
    "genre": 
    [   "HORROR",
        "ACTION"  ],
    "price": 55.0,
    "quantity": 12  }

User structure that can be passed in POST call:
{  "login": "login",
    "password": "password123"  }

## Prerequisite
    Java 17
    MySql
    
## How to run
How to run OnlineShop

>>>Run through NetBeans

Step 1: 
        a)Make connection to local server on ip : 127.0.0.1 port 3306
        a)Run MySQL database on localhost 3306, with username: "root", password: "password", not using SSL
        b)Create schema:"shopschema"
            or change details in src\main\resources\application.yml to what you prefer and adjust changes to MySql database.

Step 2: Build project

Step 3: Run class src\main\java\cdpr\web\OnlineShopApplication.java

Step 4: Communicate with server Using those calls:
    - localhost:8080/

        - Only no user
            ##POST
                * create - creates instance of user in database.
                    User is passed in body request of call.
                * login - logs onto user's profile.
                    User is passed in body request of call.
        - no user
            ##GET
                * byId/{id} - gets data of game by it's id.
                * {name} - gets datas of games of certain name.
                * available/{id} - checks if game of this id has any copy left to buy
                * all - gets data of all games.
                * all/{variable} - gets data of all games:
                                    - if variable is a number, gets all games that cost is lower that number
                                    - if variable is a type of genre, gets all games that are of that type
                                    - else, gets all games that are of this developer
        - user
            ##POST
                * logout - logs out from profile.
            ##PUT
                * {id} - buys game of this id if available in stocks
        - admin
            ##POST
                * admin - creates instance of game in stock.
                    Game is passed in body request of call. If quantity is not passed in call, it will be set to 0. 
                * admin/changeGame/{id} = changes data of game under given id.
                    Game is passed in body request of call. If quantity is not passed in call, it will be set to 0. 
            ##GET
                * admin/getUsers - gets logins and permissions of all users.
            ##PUT
                * admin/promote?login={name} - promotes passed login of user to admin.
                * admin/delete?login={name} = deletes passed login of user from database.
                * admin?id={id}&quantity={q} - supplies stocks of game of this id by "q".
                * admin/change?id={id}&name=[name}&developer={developer}&factor={f} - change data of given game
                                    - Call changes provided data. Id needs to be always provided
                                    - Factor multiples price of game of this id by "f"; f should be double.
                * admin/addGenre?id={id}&genre={genre} - Adds new Genre to game's data.
                * admin/deleteGenre?id={id}&genre={genre} - Deletes Genre type from game's data.
            ##DELETE
                * admin/{id} - deletes game from shop by it's id.
                * admin/clean - deletes all games from shop that stock number is equal to 0.

            Note: admin can use user calls. 
                  User can use no user calls.

## Comments
    I covered each edge cases, especially when passing values with http calls. 
    The return message will communicate if update/get/post/delete call was succussed or not. Special case is with finding primary key of data, if it is not found, exception message is returned.




## Contributor
* [Jan Michalec](https://github.com/cdfre0)
    - if anyone has problem with running, or have questions, I'm always available on WhatsApp +48798354298
