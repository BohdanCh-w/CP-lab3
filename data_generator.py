from mimesis import Food, Datetime
from random import choice, choices, sample, randint
from datetime import timedelta
from json import dump
import sys


food = Food('en')
date = Datetime()
food = [food.fruit, food.dish,
        food.drink, food.vegetable]


def generate_data(num, repeat=False):
    names = list(set([i for i in [choice(food)()
                                  for j in range(int(num * 1.5))] if len(i) < 28]))
    dates = [date.date(2021, 2023) for i in range(int(num * 0.6))]

    data = []
    fname = choices(names, k=num) if repeat else sample(names, k=num)

    for i in range(num):
        date = choice(dates)
        data.append({
            'name': fname[i],
            'date_produced': (date - timedelta(days=1) * randint(10, 1000)).strftime('%d-%m-%y'),
            'date_expiration': date.strftime('%d-%m-%y'),
            'price': randint(15, 400) * 25
        })
    return data


def main():
    n = len(sys.argv)
    sets = []
    for i in range(n//2):
        sets.append((sys.argv[i*2 + 1], sys.argv[i*2 + 2]))
    for i in sets:
        with open(sets[i][0], 'w') as f:
            dump(generate_data(sets[i][1], True), f, indent=4)


if __name__ == '__main__':
    main()
