## Overview
This project is an implementation of Online shop selling digital games, in which user can look up avaliable games, search for games of favourite developer, or even his favourite genre.
User can check if shop has it in stock, and buy it.

By special calls admin can update price of game, quantity in stock, add new games to repository either with or without existing stocks, and delete game by id, or all of them that do not have avaliable copy.


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
    "genre": "ACTION",
    "price": 44.0,
    "quantity": 5
}
Game structure that can be passed in POST call:
1. {
    "name": "Resident Evil 4",
    "developer": "CAPCOM",
    "genre": "HORROR",
    "price": 200.0,
}
2.  {
    "name": "Resident Evil 4",
    "developer": "CAPCOM",
    "genre": "HORROR",
    "price": 55.0,
    "quantity": 12
}

## How to run
How to run OnlineShop

>>>Run through NetBeans

Step 1: Run MySQL database on localhost 3306, with username: user, password: password and not using SSL,
            or change details in src\main\resources\application.yml

Step 2: Build project

Step 3: Run class src\main\java\cdpr\web\OnlineShopApplication.java

Step 4: Communicate with server Using those calls:
    - localhost:8080/
        - user
            ##GET
                * byId/{id} - gets data of game by it's id.
                * {name} - gets datas of games of certain name.
                * all - gets data of all games.
                * all/{variable} - gets data of all games:
                                    - if variable is a number, gets all games that cost is lower that number
                                    - if variable is a type of genre, gets all games that are of that type
                                    - else, gets all games that are of this developer
                * avalaible/{id} - checks if game of this id has any copy left to buy
            ##PUT
                * {id} - buys game of this id if avaliable in stocks
        - admin
            ##POST
                * admin - creates instance of game in stock.
                    Game is passed in body request of call. If quantity is not passed in call, it will be set to 0. 
            ##PUT
                * admin?id={id}&quantity={q} - supplies stocks of game of this id by "q"
                * admin/sale?id={id}&factor={f} - Mutiplies price ofgame of this id by "f"; f should be double
            ##DELETE
                * admin/{id} - deletes game from shop by it's id
                * admin/clean - deletes all games from shop that stock number is equal to 0


## Contributor
* [Jan Michalec](https://github.com/cdfre0)
