import requests
from datetime import datetime

MOVIES = [
    {'title': 'Cool Movie 1', 'director': 'Jan Kowalski', 'actors': '', 'duration': 120,
     'description': 'The coolest movie this year'},
    {'title': 'Why bother writing json', 'director': 'Jan Nowak', 'actors': '', 'duration': 150,
     'description': 'Exactly, why?'},
    {'title': 'My fight', 'director': 'Jan Nowak', 'actors': 'Janina Nowakowska, Hanna Nowak', 'duration': 90,
     'description': 'The title has no reference'},
]
MOVIE_IDS = []

ROOMS = [
    {'name': 'Room 1', 'seatsNumber': 200},
    {'name': 'Room 2', 'seatsNumber': 200},
    {'name': 'Room 3', 'seatsNumber': 200},
]
ROOMS_IDS = []

SEATS = [
    [{'row': j, 'number': i} for i in range(20) for j in range(10)] for k in range(3)
]
SEATS_IDS = []

SCREENINGS = []
SCREENING_IDS = []


def populate_movies():
    for movie in MOVIES:
        try:
            r = requests.post('http://localhost:9000/api/populate/movie', json=movie)
            if r.ok:
                MOVIE_IDS.append(r.json()['id'])
            else:
                raise Exception('Did not receive movie ID')
        except Exception as e:
            print(e)
            exit(1)


def populate_rooms():
    for room in ROOMS:
        try:
            r = requests.post('http://localhost:9000/api/populate/room', json=room)
            if r.ok:
                ROOMS_IDS.append(r.json()['id'])
            else:
                raise Exception('Did not receive room ID')
        except Exception as e:
            print(e)
            exit(1)


def populate_seats():
    for i, room_seats in enumerate(SEATS):
        for seat in room_seats:
            try:
                r = requests.post('http://localhost:9000/api/populate/seat',
                                  json={'roomID': ROOMS_IDS[i], **seat})
                if r.ok:
                    SEATS_IDS.append(r.json()['id'])
                else:
                    raise Exception('Did not receive seat ID')
            except Exception as e:
                print(e)
                exit(1)


def create_screenings():
    # Room 1 -----------------------------------------------------------------------------------------------------------
    SCREENINGS.append({'movieID': MOVIE_IDS[0], 'roomID': ROOMS_IDS[0],
                       'startTime': int(datetime.strptime('05.05.2022 09:30', '%d.%m.%Y %H:%M').timestamp())})
    SCREENINGS.append({'movieID': MOVIE_IDS[0], 'roomID': ROOMS_IDS[0],
                       'startTime': int(datetime.strptime('05.05.2022 12:30', '%d.%m.%Y %H:%M').timestamp())})
    SCREENINGS.append({'movieID': MOVIE_IDS[0], 'roomID': ROOMS_IDS[0],
                       'startTime': int(datetime.strptime('05.05.2022 17:30', '%d.%m.%Y %H:%M').timestamp())})

    SCREENINGS.append({'movieID': MOVIE_IDS[0], 'roomID': ROOMS_IDS[0],
                       'startTime': int(datetime.strptime('06.05.2022 10:30', '%d.%m.%Y %H:%M').timestamp())})
    SCREENINGS.append({'movieID': MOVIE_IDS[1], 'roomID': ROOMS_IDS[0],
                       'startTime': int(datetime.strptime('06.05.2022 15:30', '%d.%m.%Y %H:%M').timestamp())})
    SCREENINGS.append({'movieID': MOVIE_IDS[2], 'roomID': ROOMS_IDS[0],
                       'startTime': int(datetime.strptime('06.05.2022 20:30', '%d.%m.%Y %H:%M').timestamp())})
    # Room 2 -----------------------------------------------------------------------------------------------------------
    SCREENINGS.append({'movieID': MOVIE_IDS[0], 'roomID': ROOMS_IDS[1],
                       'startTime': int(datetime.strptime('05.05.2022 08:30', '%d.%m.%Y %H:%M').timestamp())})
    SCREENINGS.append({'movieID': MOVIE_IDS[2], 'roomID': ROOMS_IDS[1],
                       'startTime': int(datetime.strptime('05.05.2022 13:30', '%d.%m.%Y %H:%M').timestamp())})
    SCREENINGS.append({'movieID': MOVIE_IDS[2], 'roomID': ROOMS_IDS[1],
                       'startTime': int(datetime.strptime('05.05.2022 18:30', '%d.%m.%Y %H:%M').timestamp())})

    SCREENINGS.append({'movieID': MOVIE_IDS[1], 'roomID': ROOMS_IDS[1],
                       'startTime': int(datetime.strptime('06.05.2022 08:30', '%d.%m.%Y %H:%M').timestamp())})
    SCREENINGS.append({'movieID': MOVIE_IDS[1], 'roomID': ROOMS_IDS[1],
                       'startTime': int(datetime.strptime('06.05.2022 12:30', '%d.%m.%Y %H:%M').timestamp())})
    SCREENINGS.append({'movieID': MOVIE_IDS[1], 'roomID': ROOMS_IDS[1],
                       'startTime': int(datetime.strptime('06.05.2022 19:30', '%d.%m.%Y %H:%M').timestamp())})
    # Room 3 -----------------------------------------------------------------------------------------------------------
    SCREENINGS.append({'movieID': MOVIE_IDS[0], 'roomID': ROOMS_IDS[2],
                       'startTime': int(datetime.strptime('05.05.2022 11:30', '%d.%m.%Y %H:%M').timestamp())})
    SCREENINGS.append({'movieID': MOVIE_IDS[1], 'roomID': ROOMS_IDS[2],
                       'startTime': int(datetime.strptime('05.05.2022 16:30', '%d.%m.%Y %H:%M').timestamp())})
    SCREENINGS.append({'movieID': MOVIE_IDS[2], 'roomID': ROOMS_IDS[2],
                       'startTime': int(datetime.strptime('05.05.2022 20:30', '%d.%m.%Y %H:%M').timestamp())})

    SCREENINGS.append({'movieID': MOVIE_IDS[2], 'roomID': ROOMS_IDS[2],
                       'startTime': int(datetime.strptime('06.05.2022 08:30', '%d.%m.%Y %H:%M').timestamp())})
    SCREENINGS.append({'movieID': MOVIE_IDS[0], 'roomID': ROOMS_IDS[2],
                       'startTime': int(datetime.strptime('06.05.2022 17:30', '%d.%m.%Y %H:%M').timestamp())})
    SCREENINGS.append({'movieID': MOVIE_IDS[0], 'roomID': ROOMS_IDS[2],
                       'startTime': int(datetime.strptime('06.05.2022 21:30', '%d.%m.%Y %H:%M').timestamp())})


def populate_screenings():
    for screening in SCREENINGS:
        try:
            r = requests.post('http://localhost:9000/api/populate/screening', json=screening)
            if r.ok:
                SCREENING_IDS.append(r.json()['id'])
            else:
                raise Exception('Did not receive screening ID')
        except Exception as e:
            print(e)
            exit(1)


def main():
    populate_movies()
    populate_rooms()
    populate_seats()
    create_screenings()
    populate_screenings()


if __name__ == '__main__':
    main()
