## Overview
This project is an implementation of Online shop selling digital games, in which user can look up avaliable games, search for games of favourite developer, or even his favourite genre.
Program implements concept of accounts which one can create, delete and admin accounts can promote normal account to admins.
User can check if shop has it in stock, and buy it.

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
User Sructire in JSON
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

## How to run
How to run OnlineShop

>>>Run through NetBeans

Step 1: Run MySQL database on localhost 3306, with username: user, password: password and not using SSL,
            or change details in src\main\resources\application.yml

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
                * avalaible/{id} - checks if game of this id has any copy left to buy
                * all - gets data of all games.
                * all/{variable} - gets data of all games:
                                    - if variable is a number, gets all games that cost is lower that number
                                    - if variable is a type of genre, gets all games that are of that type
                                    - else, gets all games that are of this developer
        - user
            ##POST
                * logout - logs out from profile.
            ##PUT
                * {id} - buys game of this id if avaliable in stocks
        - admin
            ##POST
                * admin - creates instance of game in stock.
                    Game is passed in body request of call. If quantity is not passed in call, it will be set to 0. 
            ##GET
                * admin/getUsers - gets logins and permissions of all users.
            ##PUT
                * admin/promote?login={name} - promotes passed login of user to admin.
                * admin/delete?login={name} = deletes passed login of user from database.
                * admin?id={id}&quantity={q} - supplies stocks of game of this id by "q".
                * admin/sale?id={id}&factor={f} - Mutiplies price of game of this id by "f"; f should be double.
                * admin/addGenre?id={id}&genre={genre} - Adds new Genre to game's data.
                * admin/deleteGenre?id={id}&genre={genre} - Deletes Genre type from game's data.
            ##DELETE
                * admin/{id} - deletes game from shop by it's id.
                * admin/clean - deletes all games from shop that stock number is equal to 0.

            Note: admin can use user calls. 
                  User can use no user calls.

## Contributor
* [Jan Michalec](https://github.com/cdfre0)
