import requests
import json


def get_screenings():
    r = requests.get('http://localhost:9000/api/reservation/movie-screenings', params={'date': '2022-05-06-14-30'})
    print('SCREENINGS')
    print(json.dumps(r.json(), indent=2))


def get_available_seats():
    r = requests.get('http://localhost:9000/api/reservation/seats', params={'id': 5})
    print('AVAILABLE SEATS')
    print(json.dumps(r.json(), indent=2))


def make_reservation():
    data = {
        'name': 'Jarosław',
        'surname': 'dąbrowski-nowak',
        'screeningId': 5,
        'seatsIds': [35, 45, 56, 67],
        'tickets': {
            'Adult': 1,
            'Student': 2,
            'Child': 1
        }
    }

    r = requests.post('http://localhost:9000/api/reservation/reserve-seats', json=data)
    print('RESERVATION MADE')
    print(json.dumps(r.json(), indent=2))


def main():
    get_screenings()
    get_available_seats()
    make_reservation()


if __name__ == '__main__':
    main()
