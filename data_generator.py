from mimesis import Food, Datetime
from random import choice, choices, sample, randint
from datetime import timedelta
from json import dump


food = Food('en')
date = Datetime()
food = [food.fruit, food.dish,
        food.drink, food.vegetable]

names = list(set([i for i in [choice(food)()
             for j in range(50)] if len(i) < 28]))
dates = [date.date(2021, 2023) for i in range(15)]


def generate_data(num1, num2, repeat=False):
    data = []
    n = randint(num1, num2)
    fname = choices(names, k=n) if repeat else sample(names, k=n)

    for i in range(n):
        date = choice(dates)
        data.append({
            'name': fname[i],
            'date_produced': (date - timedelta(days=1) * randint(10, 1000)).strftime('%d-%m-%y'),
            'date_expiration': date.strftime('%d-%m-%y'),
            'price': randint(15, 400) * 25
        })
    return data


with open('data.json', 'w') as f:
    dump(generate_data(20, 35, True), f, indent=4)


with open('data1.json', 'w') as f:
    dump(generate_data(15, 25), f, indent=4)


with open('data2.json', 'w') as f:
    dump(generate_data(15, 25), f, indent=4)
